package com.passwordmanager.password_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String masterPasswordHash; //hashed password

    protected User() {
    }

    public User(String username, String email, String masterPasswordHash) {
        this.username = username;
        this.email = email;
        this.masterPasswordHash = masterPasswordHash;
    }

    public Long getId() {
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

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", masterPasswordHash='" + masterPasswordHash + '\'' +
                '}';
    }
}
