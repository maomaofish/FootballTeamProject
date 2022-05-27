package com.company.footballteam.service.impl;

import com.company.footballteam.model.Team;
import com.company.footballteam.model.Player;
import com.company.footballteam.repository.PlayerRepository;
import com.company.footballteam.repository.TeamRepository;
import com.company.footballteam.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerServiceImpl playerService;

    public TeamServiceImpl(TeamRepository teamRepository) {
        super();
        this.teamRepository = teamRepository;
    }

    @Override
    public Team saveTeam(Team team) {

        Team res = teamRepository.saveAndFlush(team);
        List<Player> playerList = playerService.generateATeam();
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            player.setTeamId(res.getId());
            playerRepository.save(player);
        }
        return res;
    }

}
