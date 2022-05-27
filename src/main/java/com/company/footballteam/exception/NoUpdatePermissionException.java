package com.company.footballteam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NoUpdatePermissionException extends RuntimeException {
    private String userName;
    private String reason;

    public NoUpdatePermissionException(String userName, String reason) {
        super(String.format("%s has no permission, reason is : %s !", userName, reason));
        this.userName = userName;
        this.reason = reason;
    }

    public String getUserName() {
        return userName;
    }

    public String getReason() {
        return reason;
    }
}
