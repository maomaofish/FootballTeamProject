package com.company.footballteam.responsestruct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseToken {
    private String token;
    public ResponseToken(String token) {
        this.token = token;
    }
}
