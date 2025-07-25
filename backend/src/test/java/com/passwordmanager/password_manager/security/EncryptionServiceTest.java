package com.passwordmanager.password_manager.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.security.EncryptionService.EncryptedData;

@ExtendWith(MockitoExtension.class)
public class EncryptionServiceTest {

  private EncryptionService encryptionService;

  @BeforeEach
  void SetUp() {
    encryptionService = new EncryptionService();
  }

  @Test
  void encryptAndDecrypt_shouldReturnOriginalPassword() throws EncryptionException {
    byte[] fixedSalt = "1234567890abcdef".getBytes(StandardCharsets.UTF_8);
    String password = "mypassword";
    String entryPassword = "entryPassword";

    SecretKey key = encryptionService.deriveKey(password, fixedSalt);
    EncryptedData encrypted = encryptionService.encrypt(entryPassword, key);
    String decrypted = encryptionService.decrypt(encrypted.ciphertext(), encrypted.iv(), key);

    assertEquals(entryPassword, decrypted);
  }

  @Test
  void passwordHashing_shouldReturnMatch() throws EncryptionException {
    String password = "mypassword";
    byte[] testSalt = encryptionService.generateSalt();
    String hash = encryptionService.hashPassword(password, testSalt);
    assertTrue(encryptionService.matches(password, hash, testSalt));
  }

}
