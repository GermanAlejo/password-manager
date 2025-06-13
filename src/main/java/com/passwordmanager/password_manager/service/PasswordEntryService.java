package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordEntryService {

    private final PasswordRepository passwordRepository;

    public PasswordEntryService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    //TODO: Function to create new entry
    public PasswordEntry createNewEntry(PasswordEntryDTO passwordEntryDTO) {
        //TODO: Get user here from spring context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()) {
            //TODO: Throw exception here
        }
        User user = (User) auth.getPrincipal();
        String userId = user.getId();

        PasswordEntry newEntry = new PasswordEntry(passwordEntryDTO.getEntryName(), passwordEntryDTO.getPassword(), userId);
        return passwordRepository.save(newEntry);
    }

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
