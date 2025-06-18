package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import com.passwordmanager.password_manager.security.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public PasswordEntry createNewEntry(PasswordEntryDTO passwordEntryDTO, User user) throws EncryptionException {
        try {
            //Encrypt the password before saving it to the db
            //Get salt from user
            byte[] decodedSalt = encryptionService.decodeSalt(user.getSalt());
            String encryptedPassword = encryptionService.encrypt(passwordEntryDTO.getPassword(), decodedSalt);
            PasswordEntry newEntry = new PasswordEntry(passwordEntryDTO.getEntryName(), encryptedPassword, user.getId());
            return passwordRepository.save(newEntry);
        } catch (GeneralSecurityException e) {
            log.error("Error decrypting entry pass");
            throw new EncryptionException("Error with encryption key");
        }
    }

    public boolean doesEntryExists(String name) {
        return passwordRepository.existsByEntryName(name);
    }

    public List<PasswordEntry> listEntriesByUserId(String userId) throws PasswordEntryNotFoundException {
        return passwordRepository.findByUserId(userId).orElseThrow(() -> new PasswordEntryNotFoundException("Not entries found for this user"));
    }

    public List<PasswordEntry> listAllEntries() {
        return passwordRepository.findAll();
    }

}
