package com.flatmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// UserDetailsServiceAutoConfiguration band: warna Spring ek default "user" + random password bana deta hai.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class FlatmateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlatmateApplication.class, args);
    }
}

