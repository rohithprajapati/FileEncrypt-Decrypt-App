package com.fileencrypt.controller;

import com.fileencrypt.service.FileEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileEncryptionController {

    @Autowired
    private FileEncryptionService fileEncryptionService;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("secretKey") String secretKey
    ) {
        try {
            byte[] encryptedBytes = fileEncryptionService.encryptFile(file, secretKey);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=encrypted_" + file.getOriginalFilename())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(encryptedBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Encryption failed: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decryptFile(
            @RequestParam("file") MultipartFile encryptedFile,
            @RequestParam("secretKey") String secretKey
    ) {
        try {
            byte[] decryptedBytes = fileEncryptionService.decryptFile(encryptedFile, secretKey);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=decrypted_" + encryptedFile.getOriginalFilename())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(decryptedBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Decryption failed: " + e.getMessage());
        }
    }

    @GetMapping("/generate-key")
    public ResponseEntity<String> generateSecretKey() {
        try {
            SecretKey secretKey = fileEncryptionService.generateSecretKey();
            String encodedKey = fileEncryptionService.getEncodedSecretKey(secretKey);
            return ResponseEntity.ok(encodedKey);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Key generation failed: " + e.getMessage());
        }
    }
}
