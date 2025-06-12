package com.passwordmanager.password_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class PasswordEntry {

    @Id
    private String id;
    private String entryName;
    @Indexed(unique = true)
    private String encryptedPassword;
    @Field("user_id")
    private String userId;

    protected PasswordEntry() {
    }

    public PasswordEntry(String entryName, String encryptedPassword, String userId) {
        this.entryName = entryName;
        this.encryptedPassword = encryptedPassword;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "id=" + id +
                ", entryName='" + entryName + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                '}';
    }
}
