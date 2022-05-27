package com.company.footballteam.responsestruct;

import com.company.footballteam.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUser {
    private String email;
    private String firstName;
    private String lastName;
    private String teamName;
    public ResponseUser(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.teamName = user.getTeamName();
    }
}
