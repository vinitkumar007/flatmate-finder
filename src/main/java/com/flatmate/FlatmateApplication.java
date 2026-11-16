package com.flatmate;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlatmateApplication {

    public static void main(String[] args) {
        @SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
    }
}