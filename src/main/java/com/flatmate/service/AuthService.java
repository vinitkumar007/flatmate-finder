package com.flatmate.service;

import com.flatmate.dto.TokenResponse;
import com.flatmate.exception.ApiException;
import com.flatmate.model.User;
import com.flatmate.repository.UserRepository;
import com.flatmate.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
public class AuthService {

    private static final int MAX_OTP_TRIES = 5;
    private static final Duration OTP_VALID = Duration.ofMinutes(10);
    private static final int MAX_FAILED_LOGINS = 5;
    private static final Duration LOCK_TIME = Duration.ofMinutes(15);

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final OtpSender otpSender;
    private final JwtService jwt;
    private final SecureRandom random = new SecureRandom();
    private final String dummyHash;

    public AuthService(UserRepository users, PasswordEncoder encoder, OtpSender otpSender, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.otpSender = otpSender;
        this.jwt = jwt;
        this.dummyHash = encoder.encode("dummy-password-for-timing");
    }

    @Transactional
    public void register(String email, String password) {
        String e = email.trim().toLowerCase();

        if (users.findByEmail(e).isPresent()) {
            return;
        }

        User u = new User();
        u.setEmail(e);
        u.setPasswordHash(encoder.encode(password));
        issueOtp(u);
        users.save(u);
    }

    private void issueOtp(User u) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        u.setOtpHash(encoder.encode(otp));
        u.setOtpExpiry(Instant.now().plus(OTP_VALID));
        u.setOtpTries(0);
        otpSender.send(u.getEmail(), otp);
    }

    @Transactional(noRollbackFor = ApiException.class)
    public void verify(String email, String otp) {
        User u = users.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ApiException(400, "OTP galat ya expire ho gaya"));

        if (u.isVerified()) {
            return;
        }
        if (u.getOtpHash() == null || u.getOtpExpiry() == null
                || Instant.now().isAfter(u.getOtpExpiry())
                || u.getOtpTries() >= MAX_OTP_TRIES) {
            throw new ApiException(400, "OTP expire ho gaya. Naya OTP mangwao.");
        }

        u.setOtpTries(u.getOtpTries() + 1);

        if (!encoder.matches(otp, u.getOtpHash())) {
            throw new ApiException(400, "OTP galat hai");
        }

        u.setVerified(true);
        u.setOtpHash(null);
        u.setOtpExpiry(null);
        u.setOtpTries(0);
    }

    @Transactional(noRollbackFor = ApiException.class)
    public TokenResponse login(String email, String password) {
        User u = users.findByEmail(email.trim().toLowerCase()).orElse(null);

        if (u == null) {
            encoder.matches(password, dummyHash);
            throw invalidLogin();
        }

        if (u.getLockUntil() != null && Instant.now().isBefore(u.getLockUntil())) {
            throw new ApiException(423, "Bahut galat attempts. Account 15 minute ke liye lock hai.");
        }

        if (!encoder.matches(password, u.getPasswordHash())) {
            u.setFailedAttempts(u.getFailedAttempts() + 1);
            if (u.getFailedAttempts() >= MAX_FAILED_LOGINS) {
                u.setLockUntil(Instant.now().plus(LOCK_TIME));
                u.setFailedAttempts(0);
            }
            throw invalidLogin();
        }

        u.setFailedAttempts(0);
        u.setLockUntil(null);

        if (u.isBanned()) {
            throw new ApiException(403, "Aapka account suspend hai");
        }
        if (!u.isVerified()) {
            throw new ApiException(403, "Pehle OTP se email verify karo");
        }

        return new TokenResponse(jwt.createAccessToken(u), u.getRole());
    }

    private ApiException invalidLogin() {
        return new ApiException(401, "Email ya password galat hai");
    }
}