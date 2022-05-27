package com.company.footballteam.service.impl;

import java.util.*;
import com.company.footballteam.exception.NoUpdatePermissionException;
import com.company.footballteam.exception.PreconditionNotSatified;
import com.company.footballteam.exception.ResourceNotFoundException;
import com.company.footballteam.model.Player;
import com.company.footballteam.model.Team;
import com.company.footballteam.repository.PlayerRepository;
import com.company.footballteam.repository.TeamRepository;
import com.company.footballteam.repository.UserRepository;
import com.company.footballteam.service.PlayerService;
import com.company.footballteam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final int MARKETVALUE = 1000000;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        super();
        this.playerRepository = playerRepository;
    }

    // private String getCurrentUser() {
    //     Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
    //     if (principal instanceof UserDetails) {
    //         return ((UserDetails) principal).getUsername();
    //     } else {
    //         return principal.toString();
    //     }
    // }

    @Override
    public Player savePlayer(Player player) {

        Player res = playerRepository.saveAndFlush(player);
        return res;
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> getAllMarketPlayers() {

        return playerRepository.findAllMarket();
    }

    @Override
    public Player getPlayerById(long id) {
        return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player", "Id", id));
    }

    /**
     * Patch only 5 fields: firstName, lastName, country, inMarket(only for : from 0
     * to 1), and askingPrice
     * Owner change above features , if loggin user is not user ,then throw
     * NoPermissionExcept
     * change inMa
     */
    @Override
    public Player updatePlayer(Player player, long id) {

        // check if the login user owns this player.
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Player currentPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        // If this player's team is not belongs to loggin user, return you are not the
        // owener of this player
        long originalTeamId = currentPlayer.getTeamId();// original team is  current player's team id; we also want the current player belong's to which user
        
        
        Team originalTeam = teamRepository.findById(originalTeamId).orElseThrow(() -> new ResourceNotFoundException("Team", "team_id", originalTeamId));          

        long originalUserId = originalTeam.getUserId(); //lina
        User loginUser = userRepository.findByEmail(userEmail); 
  

        //long ownedTeamId = user.getTeamId();lina
        
        // long ownedTeamId = team.getId();
        if (originalUserId != loginUser.getId()) {
            throw new NoUpdatePermissionException(userEmail, "Your are not the owner of this player");
        }

        // If owner wants to change info of the player already in market, then return
        // this player is no more in your team;
        if (currentPlayer.getInMarket() == 1) {
            throw new NoUpdatePermissionException(userEmail, "This player is no more in your team");
        }

        // Team team = teamRepository.findById(originalTeamId)
        //         .orElseThrow(() -> new ResourceNotFoundException("Team", "team_id", originalTeamId));

        /**
         * inMarket from request is 1 means: owner wants to sell this player
         * inMarket from request is 0 means: owner doesn't want to change this
         * attribute;
         **/
        if (player.getInMarket() == 1) {

            if (player.getAskingPrice() == 0) {
                throw new PreconditionNotSatified(originalTeam.getName(),
                        "If you want to sell this player, you must set his/her asking price");
            }
            currentPlayer.setAskingPrice(player.getAskingPrice());
            currentPlayer.setInMarket(player.getInMarket());
            // the orginal team value - player's value , becase he is not belong to the
            // original team anymore
            originalTeam.setTeamValue(originalTeam.getTeamValue() - currentPlayer.getMarketValue());
            ;
            teamRepository.save(originalTeam);
        }

        if (player.getFirstName() != null)
            currentPlayer.setFirstName(player.getFirstName());
        if (player.getLastName() != null)
            currentPlayer.setLastName(player.getLastName());
        if (player.getCountry() != null)
            currentPlayer.setCountry(player.getCountry());

        playerRepository.save(currentPlayer);
        return currentPlayer;
    }

    public Player buyPlayer(long id) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        // If this player's owner is the login user, then no need to buy again
        long originalTeamId = player.getTeamId();

        Team originalTeam = teamRepository.findById(originalTeamId)
            .orElseThrow(() -> new ResourceNotFoundException("Team", "id", originalTeamId));

        long originalUserId = originalTeam.getUserId();

        User user = userRepository.findByEmail(userEmail);
        //long ownedTeamId = user.getTeamId();
        
        if (user.getId() == originalUserId) {
            throw new NoUpdatePermissionException(userEmail, "You have this players already, no need to buy again");
        }

        Team newTeam = teamRepository.getbyUserId(user.getId());
                

        // Player is not in market, user can not buy him into own team.
        if (player.getInMarket() == 0) {
            throw new NoUpdatePermissionException(userEmail, "This player is not in market, you can't buy him/her now");
        }

        if (newTeam.getBudget() < player.getAskingPrice()) {
            throw new PreconditionNotSatified(newTeam.getName(), "budget is less than asking price");
        }
        // make the buyer team budget minus the player's asking price;
        newTeam.setBudget(newTeam.getBudget() - player.getAskingPrice());
        player.setInMarket(0);

        // make the original team get the asking price, original team budget+asking
        // price
        Team oldTeam = teamRepository.findById(originalTeamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", originalTeamId));

        oldTeam.setBudget(oldTeam.getBudget() + player.getAskingPrice());
        teamRepository.saveAndFlush(oldTeam);

        // based on the current market value, get a new 110%-200% * current value.
        Random rand = new Random();
        long newValue = (player.getMarketValue() * rand.nextInt(110, 201)) / 100;

        // update the team's new value
        newTeam.setTeamValue(newTeam.getTeamValue() + newValue /*- player.getMarketValue()*/);
        teamRepository.saveAndFlush(newTeam);

        // update this player's new value and asking price
        player.setMarketValue(newValue);
        player.setAskingPrice(newValue);
        player.setTeamId(newTeam.getId());
        playerRepository.saveAndFlush(player);

        return player;
    }

    /**
     * PlayType: 1: goalkeepers
     * 2: defenders
     * 3: midfielders
     * 4: attackers
     * 
     * @return List of Plyaers: total 20 players
     */
    public List<Player> generateATeam() {
        String firstName = "FirstName";
        String lastName = "LastName";
        String country = "Country";
        List<Player> res = new ArrayList<>();
        int c = 1;
        for (int i = 0; i < 3; i++) {

            Player player = generateAPlayer(country + " " + c, firstName + " " + c, lastName + " " + c, 1);
            res.add(player);
            c++;
        }
        for (int i = 0; i < 6; i++) {
            Player player = generateAPlayer(country + " " + c, firstName + " " + c, lastName + " " + c, 2);
            res.add(player);
            c++;
        }
        for (int i = 0; i < 6; i++) {
            Player player = generateAPlayer(country + " " + c, firstName + " " + c, lastName + " " + c, 3);
            res.add(player);
            c++;
        }
        for (int i = 0; i < 5; i++) {
            Player player = generateAPlayer(country + " " + c, firstName + " " + c, lastName + " " + c, 4);
            res.add(player);
            c++;
        }
        return res;
    }

    public Player generateAPlayer(String country, String firstName, String lastName, int playerType) {

        Player newPlayer = new Player();

        Random rand = new Random();
        newPlayer.setAge(rand.nextInt(18, 41));
        newPlayer.setCountry(country);
        newPlayer.setFirstName(firstName);
        newPlayer.setLastName(lastName);
        newPlayer.setMarketValue(MARKETVALUE);
        newPlayer.setAskingPrice(MARKETVALUE);
        newPlayer.setPlayerType(playerType);
        newPlayer.setInMarket(0);
        return newPlayer;
    }
}