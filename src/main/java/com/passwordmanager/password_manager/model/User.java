package com.passwordmanager.password_manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String masterPasswordHash; //hashed password
    private byte[] salt; //for encryption key derivation

    protected User() {
    }

    public User(String username, String email, byte[] salt) {
        this.username = username;
        this.email = email;
        this.salt = salt;
    }

    public User(String username, String email, String masterPasswordHash, byte[] salt) {
        this.username = username;
        this.email = email;
        this.masterPasswordHash = masterPasswordHash;
        this.salt = salt;
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getMasterPasswordHash() {
        return this.masterPasswordHash;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMasterPasswordHash(String masterPasswordHash) {
        this.masterPasswordHash = masterPasswordHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(final byte[] salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", masterPasswordHash='" + masterPasswordHash + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
