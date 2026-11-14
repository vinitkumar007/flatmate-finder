package com.flatmate.dto;

import jakarta.validation.constraints.*;

import java.time.Instant;

/** Sab request/response classes ek jagah. Validation annotations yahin lagi hain. */
public final class Dtos {
    private Dtos() {}

    // ---------- Auth ----------
    public record RegisterRequest(
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,72}$",
                    message = "Password 8-72 characters ka ho, usme capital letter, small letter aur number ho") String password) {}

    public record VerifyRequest(
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Pattern(regexp = "^\\d{6}$", message = "OTP 6 digit ka hota hai") String otp) {}

    public record EmailRequest(@NotBlank @Email @Size(max = 120) String email) {}

    public record LoginRequest(
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Size(max = 72) String password) {}

    public record RefreshRequest(@NotBlank @Size(max = 200) String refreshToken) {}

    public record TokenResponse(String accessToken, String refreshToken, String role) {}

    // ---------- Profile ----------
    public record ProfileRequest(
            @NotBlank @Size(max = 60) String name,
            @Min(18) @Max(80) int age,
            @NotBlank @Pattern(regexp = "MALE|FEMALE|OTHER") String gender,
            @NotBlank @Size(max = 60) String occupation,
            @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "Phone number sahi daalo (10-13 digit)") String phone,
            @NotBlank @Size(max = 40) String city,
            @NotBlank @Size(max = 40) String area,
            @Min(1000) @Max(200000) int budget,
            @Min(0) @Max(23) int sleepHour,
            @Min(0) @Max(23) int wakeHour,
            boolean smoking,
            boolean drinking,
            boolean vegetarian,
            @Min(1) @Max(5) int cleanliness,
            @Size(max = 300) String bio,
            @NotBlank @Pattern(regexp = "ANY|MALE|FEMALE|OTHER") String prefGender,
            @Min(18) @Max(100) int prefMinAge,
            @Min(18) @Max(100) int prefMaxAge,
            boolean prefSmokingOk,
            boolean prefDrinkingOk,
            boolean prefVegOnly,
            boolean visible) {}

    /** Dusre users ko jo dikhta hai. Phone yahan NAHI hai. */
    public record Suggestion(
            Long userId, String name, int age, String gender, String occupation, String area, int budget,
            int sleepHour, int wakeHour, boolean smoking, boolean drinking, boolean vegetarian,
            int cleanliness, String bio, int score) {}

    public record RequestView(Long id, Long fromUserId, String name, int age, String area, int budget, int score) {}

    /** Phone sirf accepted connection ko milta hai. */
    public record Connection(Long userId, String name, String phone, String area) {}

    // ---------- Chat / Report ----------
    public record ChatSend(@NotBlank @Size(max = 500) String text) {}

    public record MessageView(Long id, boolean fromMe, String text, Instant createdAt) {}

    public record ReportRequest(@NotBlank @Size(max = 300) String reason) {}
}
