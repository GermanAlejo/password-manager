package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.controller.PasswordEntryController;
import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.IllegalPasswordEntryException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import com.passwordmanager.password_manager.security.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class PasswordEntryService {

    private static final Logger log = LoggerFactory.getLogger(PasswordEntryService.class);

    private final EncryptionService encryptionService;
    private final PasswordRepository passwordRepository;

    public PasswordEntryService(EncryptionService encryptionService, PasswordRepository passwordRepository) {
        this.encryptionService = encryptionService;
        this.passwordRepository = passwordRepository;
    }

    //TODO: Function to create new entry
    public PasswordEntry createNewEntry(PasswordEntryDTO passwordEntryDTO) throws EncryptionException, IllegalPasswordEntryException {
        try {
            //TODO: Get user here from spring context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.isAuthenticated()) {
                log.error("User is not authenticated");
                throw new BadCredentialsException("Credentials expired for this session");
            }
            if(doesEntryExists(passwordEntryDTO.getEntryName())) {
                log.error("Entry already created");
                throw new IllegalPasswordEntryException("Entry already created");
            }

            //TODO: not sure this works
            //Get user eiher from session or DB (Whatever is more proper)
            User user = (User) auth.getPrincipal();
            String userId = user.getId();

            //Encrypt the password before saving it to the db
            //Get salt from user
            byte[] decodedSalt = encryptionService.decodeSalt(user.getSalt());
            String encryptedPassword = encryptionService.encrypt(passwordEntryDTO.getPassword(), decodedSalt);
            PasswordEntry newEntry = new PasswordEntry(passwordEntryDTO.getEntryName(), encryptedPassword, userId);
            return passwordRepository.save(newEntry);
        } catch (GeneralSecurityException e) {
            log.error("Error decrypting entry pass");
            throw new EncryptionException("Error with encryption key");
        }
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
