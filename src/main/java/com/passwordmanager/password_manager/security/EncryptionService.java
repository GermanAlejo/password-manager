package com.passwordmanager.password_manager.security;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmConstraints;
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
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String PDKDF_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String KEY_ALGORIGTHM = "AES";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);



    //TODO: CHECK OPTIONS HERE FOR ALL METHODS
    public SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
            log.info("Generating new key");
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PDKDF_ALGORITHM);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), KEY_ALGORIGTHM);
    }

    public String encrypt(String plainText, byte[] salt)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        SecretKey key = deriveKey(plainText, salt);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText, byte[] salt)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        SecretKey key = deriveKey(encryptedText, salt);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PDKDF_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean matches(String password, String hashedPassword, byte[] salt) throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
            try {
                String inputHash = hashPassword(password, salt);
                return Objects.equals(inputHash, hashedPassword);
            } catch (Exception e) {
                return false;
            }
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public String encodeSalt(byte[] salt) {
        return Base64.getEncoder().encodeToString(salt);
    }

    public byte[] decodeSalt(String saltEncoded) {
        return Base64.getDecoder().decode(saltEncoded);
    }

    public String byteToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }



}
