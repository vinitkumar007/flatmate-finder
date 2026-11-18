package com.flatmate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta,validation.constraints.Size;

public record ProfileRequest(
    
    @NotBlank @Size(Max =60)
    String name,

    @Min (18) @Max(80)
    int age,

    @NotBlank @Pattern(regexp = "MALE|FEMALE|OTHER")
    String gender,
    @NotBlank@Size(Max = 60)
    String occupation,

    @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "invalid phone number")
    String phone,
    @NotBlank @Size(Max = 40)
    String city,

    @NotBlank @Size(Max = 40)
    String area,

    @Min(1000) @Max(200000)
    int budget

) {}
