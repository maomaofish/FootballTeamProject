package com.company.footballteam.repository;

import com.company.footballteam.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query(value = "SELECT t FROM Team t WHERE t.userId = userId")
    Team getbyUserId(long userId);
}
