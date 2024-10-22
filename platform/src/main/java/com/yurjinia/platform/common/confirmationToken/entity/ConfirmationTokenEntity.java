package com.yurjinia.platform.common.confirmationToken.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "confirmation_token")
@NoArgsConstructor
public class ConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "project_name")
    private String projectName;

    public ConfirmationTokenEntity(String token, LocalDateTime createdAt, LocalDateTime expiresAt, String userEmail, String projectName) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userEmail = userEmail;
        this.projectName = projectName;
    }

    public ConfirmationTokenEntity(String token, LocalDateTime createdAt, LocalDateTime expiresAt, String userEmail) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userEmail = userEmail;
    }

}

