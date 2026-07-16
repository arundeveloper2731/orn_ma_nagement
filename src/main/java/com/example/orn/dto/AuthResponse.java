package com.example.orn.dto;

public class AuthResponse {

    private String message;
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String role;

    public AuthResponse() {
    }

    public AuthResponse(String message, String token, String username, String role) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
