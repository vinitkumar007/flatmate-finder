package com.flatmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatSend(

        @NotBlank @Size(max = 500)
        String text
) {}