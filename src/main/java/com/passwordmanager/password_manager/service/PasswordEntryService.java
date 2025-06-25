package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
import com.passwordmanager.password_manager.model.KeyCache;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import com.passwordmanager.password_manager.security.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

@Service
public class PasswordEntryService {

  private static final Logger log = LoggerFactory.getLogger(PasswordEntryService.class);

  private final EncryptionService encryptionService;
  private final UserService userService;
  private final PasswordRepository passwordRepository;
  private final KeyCache keyCache;

  public PasswordEntryService(EncryptionService encryptionService, UserService userService, PasswordRepository passwordRepository, KeyCache keyCache) {
    this.encryptionService = encryptionService;
    this.userService = userService;
    this.passwordRepository = passwordRepository;
    this.keyCache = keyCache;
  }

  public PasswordEntry createNewEntry(PasswordEntryDTO passwordEntryDTO, User user, String jwt) throws EncryptionException {
    try {
      //TODO: Check if this is optinal & revist this exception
      //Encrypt the password before saving it to the db
      //First get the key from cache
      SecretKey masterKey = keyCache.get(user.getId(), jwt)
              .orElseThrow(() -> new SecurityException("Session expired or invalid"));

      //encrypt with master key
      EncryptionService.EncryptedData encryptedData = encryptionService.encrypt(passwordEntryDTO.getPassword(), masterKey);
      PasswordEntry newEntry = new PasswordEntry(passwordEntryDTO.getEntryName(), encryptedData.ciphertext(), encryptedData.iv(), user.getId());
      return passwordRepository.save(newEntry);
    }
    catch (GeneralSecurityException e) {
      log.error("Error decrypting entry pass");
      throw new EncryptionException("Error with encryption key");
    }
  }

  public boolean doesEntryExists(String name) {
    return passwordRepository.existsByEntryName(name);
  }

  public List<PasswordEntryDTO> listEntriesByUserId(String userId, String jwt) throws PasswordEntryNotFoundException, UserNotFoundException {

    List<PasswordEntry> allEntriesEncrypted =
        passwordRepository.findByUserId(userId).orElseThrow(() -> new PasswordEntryNotFoundException("Not entries found for this user"));

    //Get master key to decrypt
    SecretKey masterKey = keyCache.get(userId, jwt)
            .orElseThrow(() -> new SecurityException("Session expired or invalid"));
    //TODO: BUG HERE decrypting is broken
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
    catch (GeneralSecurityException e) {
      log.error("Encryption error", e);
      return Optional.empty();
    }
  }

  public List<PasswordEntry> listAllEntries() {
    return passwordRepository.findAll();
  }

}
