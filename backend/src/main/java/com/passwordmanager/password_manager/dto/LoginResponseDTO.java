package com.passwordmanager.password_manager.dto;

import java.util.Date;

public class LoginResponseDTO {

    private String token;
    private Date expirationDate;

    protected LoginResponseDTO(){}

    public LoginResponseDTO(String token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
