package com.flatmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportRequest(

        @NotBlank @Size(max = 300)
        String reason
) {}