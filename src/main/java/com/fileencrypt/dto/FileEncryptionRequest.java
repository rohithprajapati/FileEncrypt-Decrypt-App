package com.fileencrypt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEncryptionRequest {
    private String fileName;
    private String secretKey;
    private String encryptionMode; // "ENCRYPT" or "DECRYPT"
    private Long userId;
}
