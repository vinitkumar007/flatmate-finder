package com.flatmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    private boolean verified;

    private String otpHash;
    private Instant otpExpiry;
    private int otpTries;

    @Column(nullable = false, length = 10)
    private String role = "USER";

    private boolean banned;

    private int failedAttempts;

    private Instant lockUntil;
}