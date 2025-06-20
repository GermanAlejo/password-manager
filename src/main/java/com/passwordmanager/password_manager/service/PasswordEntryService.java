package com.passwordmanager.password_manager.service;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
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

@Service
public class PasswordEntryService {

  private static final Logger log = LoggerFactory.getLogger(PasswordEntryService.class);

  private final EncryptionService encryptionService;
  private final UserService userService;
  private final PasswordRepository passwordRepository;

  public PasswordEntryService(EncryptionService encryptionService, UserService userService, PasswordRepository passwordRepository) {
    this.encryptionService = encryptionService;
    this.userService = userService;
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
    }
    catch (GeneralSecurityException e) {
      log.error("Error decrypting entry pass");
      throw new EncryptionException("Error with encryption key");
    }
  }

  public boolean doesEntryExists(String name) {
    return passwordRepository.existsByEntryName(name);
  }

  public List<PasswordEntryDTO> listEntriesByUserId(String userId) throws PasswordEntryNotFoundException, UserNotFoundException {

    List<PasswordEntry> allEntriesEncrypted =
        passwordRepository.findByUserId(userId).orElseThrow(() -> new PasswordEntryNotFoundException("Not entries found for this user"));

    //get salt to decrypt
    String salt = userService.getUserById(userId).getSalt();
    byte[] decodedSalt = encryptionService.decodeSalt(salt);
    //TODO: BUG HERE decrypting is broken
    return allEntriesEncrypted.stream().map(entry -> decryptEntry(decodedSalt, entry)).flatMap(Optional::stream).toList();
  }

  public Optional<PasswordEntryDTO> decryptEntry(byte[] salt, PasswordEntry entry) {
    try {
      String decryptedPass = encryptionService.decrypt(entry.getEncryptedPassword(), salt);
      PasswordEntryDTO newEntry = new PasswordEntryDTO(entry.getEntryName(), decryptedPass);
      return Optional.of(newEntry);
    }
    catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException |
           InvalidKeySpecException e) {
      log.error("Encryption error", e);
      return Optional.empty();
    }
  }

  public List<PasswordEntry> listAllEntries() {
    return passwordRepository.findAll();
  }

}
