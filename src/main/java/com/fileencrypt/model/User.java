package com.fileencrypt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FileMetadata> files;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Enum for user roles
    public enum UserRole {
        ROLE_USER,
        ROLE_ADMIN
    }

    // Pre-persist method to set creation time
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
