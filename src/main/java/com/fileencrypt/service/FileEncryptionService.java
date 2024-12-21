package com.fileencrypt.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class FileEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    // Generate a secret key
    public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256); // 256-bit key
        return keyGenerator.generateKey();
    }

    // Encrypt file
    public byte[] encryptFile(MultipartFile file, String secretKeyString) throws Exception {
        // Convert secret key string to SecretKey object
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        SecretKey secretKey = new SecretKeySpec(decodedKey, ALGORITHM);

        // Initialize cipher for encryption
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Read file and encrypt
        byte[] fileBytes = file.getBytes();
        byte[] encryptedBytes = cipher.doFinal(fileBytes);

        return encryptedBytes;
    }

    // Decrypt file
    public byte[] decryptFile(MultipartFile encryptedFile, String secretKeyString) throws Exception {
        // Convert secret key string to SecretKey object
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        SecretKey secretKey = new SecretKeySpec(decodedKey, ALGORITHM);

        // Initialize cipher for decryption
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Read encrypted file and decrypt
        byte[] encryptedBytes = encryptedFile.getBytes();
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return decryptedBytes;
    }

    // Convert SecretKey to Base64 encoded string for storage/transmission
    public String getEncodedSecretKey(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Additional utility methods for file encryption
    public String encryptText(String text, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptText(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}