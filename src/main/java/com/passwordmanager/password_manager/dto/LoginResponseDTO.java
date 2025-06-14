package com.passwordmanager.password_manager.dto;


public class LoginResponseDTO {

    private String token;

    protected LoginResponseDTO(){}

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
