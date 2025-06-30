package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.InvalidArgumentsException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.KeyCache;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import com.passwordmanager.password_manager.security.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;

@Service
public class PasswordEntryService {

  private static final Logger log = LoggerFactory.getLogger(PasswordEntryService.class);

  private final EncryptionService encryptionService;
  private final PasswordRepository passwordRepository;
  private final KeyCache keyCache;

  public PasswordEntryService(EncryptionService encryptionService, PasswordRepository passwordRepository, KeyCache keyCache) {
    this.encryptionService = encryptionService;
    this.passwordRepository = passwordRepository;
    this.keyCache = keyCache;
  }

  public PasswordEntry createNewEntry(PasswordEntryDTO passwordEntryDTO, User user, String jwt) throws EncryptionException {
    log.info("Saving new entry");
    //Encrypt the password before saving it to the db
    //First get the key from cache
    SecretKey masterKey = keyCache.get(user.getId(), jwt).orElseThrow(() -> new SecurityException("Session expired or invalid"));

    //encrypt with master key
    EncryptionService.EncryptedData encryptedData = encryptionService.encrypt(passwordEntryDTO.getPassword(), masterKey);
    PasswordEntry newEntry = new PasswordEntry(passwordEntryDTO.getEntryName(), encryptedData.ciphertext(), encryptedData.iv(), user.getId());
    return passwordRepository.save(newEntry);
  }

  public boolean doesEntryExists(String name) {
    return passwordRepository.existsByEntryName(name);
  }

  public List<PasswordEntryDTO> listEntriesByUserId(String userId, String jwt) throws PasswordEntryNotFoundException {
    log.info("Retrieving entires");
    List<PasswordEntry> allEntriesEncrypted =
        passwordRepository.findByUserId(userId).orElseThrow(() -> new PasswordEntryNotFoundException("Not entries found for this user"));

    //Get master key to decrypt
    SecretKey masterKey = keyCache.get(userId, jwt).orElseThrow(() -> new SecurityException("Session expired or invalid"));
    return allEntriesEncrypted.stream().map(entry -> decryptEntry(masterKey, entry)).flatMap(Optional::stream).toList();
  }

  public Optional<PasswordEntryDTO> decryptEntry(SecretKey masterKey, PasswordEntry entry) {
    try {
      //Before decrypting we try to recover the iv
      String iv = entry.getIv();
      String decryptedPass = encryptionService.decrypt(entry.getEncryptedPassword(), iv, masterKey);
      PasswordEntryDTO newEntry = new PasswordEntryDTO(entry.getEntryName(), decryptedPass);
      return Optional.of(newEntry);
    }
    catch (EncryptionException e) {
      log.warn("Could not decrypt entry {}: {}", entry.getEntryName(), e.getMessage());
      return Optional.empty();
    }
  }

  public void deleteEntry(String name) {
    log.info("Deleting entry");
    if (name == null || name.trim().isEmpty()) {
      throw new InvalidArgumentsException("");
    }

    passwordRepository.deleteByEntryName(name);
  }

  public List<PasswordEntry> listAllEntries() {
    return passwordRepository.findAll();
  }

}
