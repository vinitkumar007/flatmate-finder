package com.flatmate.dto;

public record RequestView(Long id, Long fromUserId, String name, int age,
                          String area, int budget, int score) {}