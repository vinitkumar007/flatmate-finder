package com.flatmate.controller;

import com.flatmate.dto.LoginRequest;
import com.flatmate.dto.RegisterRequest;
import com.flatmate.dto.TokenResponse;
import com.flatmate.dto.VerifyRequest;
import com.flatmate.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterRequest r) {
        auth.register(r.email(), r.password());
        return Map.of("message", "Agar email sahi hai to OTP bhej diya gaya hai");
    }

    @PostMapping("/verify")
    public Map<String, String> verify(@Valid @RequestBody VerifyRequest r) {
        auth.verify(r.email(), r.otp());
        return Map.of("message", "Email verify ho gaya");
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest r) {
        return auth.login(r.email(), r.password());
    }
}