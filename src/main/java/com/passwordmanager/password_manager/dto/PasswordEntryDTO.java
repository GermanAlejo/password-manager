package com.passwordmanager.password_manager.dto;

import jakarta.validation.constraints.NotNull;

public class PasswordEntryDTO {

    @NotNull
    private String entryName;
    @NotNull
    private String password;
    @NotNull
    private String userName;

    protected PasswordEntryDTO() {}

    public PasswordEntryDTO(String entryName, String password) {
        this.entryName = entryName;
        this.password = password;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
