package com.company.footballteam.repository;

import java.util.List;
import com.company.footballteam.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "SELECT p FROM Player p WHERE p.inMarket = 1")
    public List<Player> findAllMarket();
}
