package com.passwordmanager.password_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

    private String username;
    @Email
    private String email;
    @NotNull
    @Size(min = 8, message = "at least 8 characters")
    private String password;

    protected LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public boolean isValid() {
        return (username != null && !username.isBlank()) ||
                (email != null && !email.isBlank());
    }

    public String getLoginIdentifier() {
        if (username != null && !username.isBlank()) {
            return username;
        }
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
