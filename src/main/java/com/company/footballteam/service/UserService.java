package com.company.footballteam.service;

import com.company.footballteam.model.Team;
import com.company.footballteam.model.User;

public interface UserService {
    User saveUser(User user);

    Team getAllTeamPlayersByLoginUser();
}
