package com.passwordmanager.password_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PasswordEntry {

    @Id
    private Long id;
    private String entryName;
    private String username;
    @Indexed(unique = true)
    private String encryptedPassword;

    private PasswordEntry() {
    }

    public PasswordEntry(String entryName, String encryptedPassword) {
        this.entryName = entryName;
        this.encryptedPassword = encryptedPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "id=" + id +
                ", entryName='" + entryName + '\'' +
                ", username='" + username + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                '}';
    }
}
