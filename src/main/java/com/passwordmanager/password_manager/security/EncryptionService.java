package com.passwordmanager.password_manager.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

  private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 128; // 128-bit authentication tag
  private static final int IV_LENGTH = 12; // 96-bit IV (recommended for GCM)
  private static final String PDKDF_ALGORITHM = "PBKDF2WithHmacSHA256";
  private static final String KEY_ALGORIGTHM = "AES";
  private static final int ITERATIONS = 65536;
  private static final int KEY_LENGTH = 256;

  private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);

  public record EncryptedData(String ciphertext, String iv) {}

  public SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    log.info("Generating new key");
    SecretKeyFactory factory = SecretKeyFactory.getInstance(PDKDF_ALGORITHM);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
    SecretKey tmp = factory.generateSecret(spec);
    return new SecretKeySpec(tmp.getEncoded(), KEY_ALGORIGTHM);
  }

  public EncryptedData encrypt(String plainText, SecretKey masterKey) throws GeneralSecurityException, InvalidKeyException {
    // Generate random IV
    byte[] iv = new byte[IV_LENGTH];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(iv);

    // Initialize cipher with GCM
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
    cipher.init(Cipher.ENCRYPT_MODE, masterKey, gcmParameterSpec);

    //Encrypt and encode
    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    String cipherText = Base64.getEncoder().encodeToString(encryptedBytes);
    String ivBase64 = Base64.getEncoder().encodeToString(iv);

    return new EncryptedData(cipherText, ivBase64);
  }

  public String decrypt(String encryptedText, String ivBase64, SecretKey masterKey) throws GeneralSecurityException, InvalidKeyException {

    byte[] iv = decodeBase64(ivBase64);
    if(iv.length != IV_LENGTH) {
      log.error("Invalid IV Length");
      throw new IllegalArgumentException("Invalid IV Length");
    }

    // Decode IV and ciphertext
    byte[] encryptedBytes = decodeBase64(encryptedText);

    // Initialize cipher with GCM
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
    cipher.init(Cipher.DECRYPT_MODE, masterKey, gcmParameterSpec);

    //Decrypt
    byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
    return byteToString(decryptedBytes);
  }

  public String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance(PDKDF_ALGORITHM);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
    byte[] hash = factory.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(hash);
  }

  public boolean matches(String password, String hashedPassword, byte[] salt) throws GeneralSecurityException {
    try {
      String inputHash = hashPassword(password, salt);
      return Objects.equals(inputHash, hashedPassword);
    }
    catch (Exception e) {
      return false;
    }
  }

  public byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }

  public String encodeBase64(byte[] salt) {
    return Base64.getEncoder().encodeToString(salt);
  }

  public byte[] decodeBase64(String saltEncoded) {
    return Base64.getDecoder().decode(saltEncoded);
  }

  public String byteToString(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

}
