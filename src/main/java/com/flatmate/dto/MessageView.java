package com.flatmate.dto;

import java.time.Instant;

public record MessageView(Long id, boolean fromMe, String text, Instant createdAt) {}