package com.passwordmanager.password_manager.repository;

import com.passwordmanager.password_manager.model.PasswordEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordRepository extends MongoRepository<PasswordEntry, String> {
    // Inherits methods like save(), findById(), findAll(), deleteById(), etc.
    //Custom methods
    Optional<PasswordEntry> findByEntryName(String entryName);
    Optional<List<PasswordEntry>> findByUserId(String userId);
    //TODO: add delete and edit method
    boolean existsByEntryName(String entryName);
}
