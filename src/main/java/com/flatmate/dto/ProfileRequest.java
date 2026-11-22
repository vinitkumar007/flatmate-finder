package com.flatmate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileRequest(

        @NotBlank @Size(max = 60)
        String name,

        @Min(18) @Max(80)
        int age,

        @NotBlank @Pattern(regexp = "MALE|FEMALE|OTHER")
        String gender,

        @NotBlank @Size(max = 60)
        String occupation,

        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "Phone number sahi daalo (10-13 digit)")
        String phone,

        @NotBlank @Size(max = 40)
        String city,

        @NotBlank @Size(max = 40)
        String area,

        @Min(1000) @Max(200000)
        int budget,

        @Min(0) @Max(23)
        int sleepHour,

        @Min(0) @Max(23)
        int wakeHour,

        @Min(1) @Max(5)
        int cleanliness,

        boolean smoking,
        boolean drinking,
        boolean vegetarian,

        @NotBlank @Pattern(regexp = "ANY|MALE|FEMALE|OTHER")
        String prefGender,

        @Min(18) @Max(100)
        int prefMinAge,

        @Min(18) @Max(100)
        int prefMaxAge,

        boolean prefSmokingOk,
        boolean prefDrinkingOk,
        boolean prefVegOnly,
        boolean visible
) {}