package com.flatmate.service;

import com.flatmate.model.User;
import com.flatmate.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public void register(String email, String password) {
        String e = email.trim().toLowerCase();

        if (users.findByEmail(e).isPresent()) {
            return;
        }

        User u = new User();
        u.setEmail(e);
        u.setPasswordHash(encoder.encode(password));
        users.save(u);
    }
}