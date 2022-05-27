package com.company.footballteam.controller;

import com.company.footballteam.model.Player;
import com.company.footballteam.service.impl.PlayerServiceImpl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PlayerServiceImpl playerService;

    public PlayerController(PlayerServiceImpl playerService) {
        super();
        this.playerService = playerService;
    }

    // @GetMapping("/players")
    // public List<Player> getAllPlayers() {
    //     return playerService.getAllPlayers();
    // }

    @GetMapping("/market_players")
    public List<Player> getAllMarketPlayers() {
        return playerService.getAllMarketPlayers();
    }

    // http://localhost:8080/api/players/1
    // @GetMapping("{id}")
    // public ResponseEntity<Player> getPlayerById(@PathVariable("id") long id) {
    // return new ResponseEntity<Player>(playerService.getPlayerById(id),
    // HttpStatus.OK);
    // }

    @PatchMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") long id, @RequestBody Player player) {

        return new ResponseEntity<Player>(playerService.updatePlayer(player, id), HttpStatus.OK);
    }

    // buy this player
    @PutMapping("/market_players/{id}")
    public ResponseEntity<Player> buyPlayer(@PathVariable("id") long id) {
        return new ResponseEntity<Player>(playerService.buyPlayer(id), HttpStatus.OK);
    }

}
