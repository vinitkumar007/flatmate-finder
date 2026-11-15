package com.flatmate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OtpSender {

    private static final Logger log = LoggerFactory.getLogger(OtpSender.class);

    public void send(String email, String otp) {
        log.info("[DEV MODE] {} ka OTP = {}", email, otp);
    }
}