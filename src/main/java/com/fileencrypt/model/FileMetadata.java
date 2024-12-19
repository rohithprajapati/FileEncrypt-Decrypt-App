package com.fileencrypt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_metadata")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String encryptedFileName;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false)
    private String encryptionAlgorithm;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    // Enum for file status
    public enum FileStatus {
        UPLOADED,
        ENCRYPTED,
        DECRYPTED,
        DELETED
    }

    // Pre-persist method to set upload time
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
