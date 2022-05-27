package com.company.footballteam.service;

import java.util.List;

import com.company.footballteam.model.Player;

public interface PlayerService {

    Player savePlayer(Player player);

    List<Player> getAllPlayers();

    List<Player> getAllMarketPlayers();

    Player getPlayerById(long id);

    Player updatePlayer(Player player, long id);
}
