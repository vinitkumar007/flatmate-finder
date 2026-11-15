package com.flatmate.service;

import com.flatmate.exception.ApiException;
import com.flatmate.model.User;
import com.flatmate.repository.UserRepository;
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

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final OtpSender otpSender;
    private final SecureRandom random = new SecureRandom();

    public AuthService(UserRepository users, PasswordEncoder encoder, OtpSender otpSender) {
        this.users = users;
        this.encoder = encoder;
        this.otpSender = otpSender;
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
}