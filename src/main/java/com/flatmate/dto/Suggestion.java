package com.flatmate.dto;

public record Suggestion(
        Long userId, String name, int age, String gender, String occupation,
        String area, int budget, int sleepHour, int wakeHour, int cleanliness,
        boolean smoking, boolean drinking, boolean vegetarian, int score) {}