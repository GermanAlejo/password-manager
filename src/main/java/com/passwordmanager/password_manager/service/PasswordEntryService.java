package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordEntryService {

    private final PasswordRepository passwordRepository;

    public PasswordEntryService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    //TODO: Function to create new entry


    public boolean doesEntryExists(String name) {
        return passwordRepository.existsByEntryName(name);
    }

    public List<PasswordEntry> listEntriesByUser(String userId) throws PasswordEntryNotFoundException {
        return passwordRepository.findByUserId(userId).orElseThrow(() -> new PasswordEntryNotFoundException("Not entries found for this user"));
    }

    public List<PasswordEntry> listAllEntries() {
        return passwordRepository.findAll();
    }

}
