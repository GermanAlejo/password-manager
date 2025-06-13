package com.passwordmanager.password_manager.dto;


public class AuthResponseDTO {

    private String token;

    protected AuthResponseDTO(){}

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
