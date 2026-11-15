package com.flatmate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyRequest(

        @NotBlank @Email @Size(max = 120)
        String email,

        @NotBlank @Pattern(regexp = "^\\d{6}$", message = "OTP 6 digit ka hota hai")
        String otp
) {}