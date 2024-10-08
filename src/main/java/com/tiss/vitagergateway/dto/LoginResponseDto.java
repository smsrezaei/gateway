package com.tiss.vitagergateway.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }

}