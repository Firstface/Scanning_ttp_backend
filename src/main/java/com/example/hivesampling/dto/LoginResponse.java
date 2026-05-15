package com.example.hivesampling.dto;

public class LoginResponse {
    public String token;
    public String type = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }
}
