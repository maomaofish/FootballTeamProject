package com.company.footballteam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class PreconditionNotSatified extends RuntimeException {
    private String teamName;
    private String reason;

    public PreconditionNotSatified(String teamName, String reason) {
        super(String.format("precondition is not satisfied in team %s, reason is : %s !", teamName, reason));
        this.teamName = teamName;
        this.reason = reason;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getReason() {
        return reason;
    }
}
