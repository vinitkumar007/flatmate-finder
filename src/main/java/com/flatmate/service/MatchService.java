package com.flatmate.service;

import com.flatmate.dto.Suggestion;
import com.flatmate.exception.ApiException;
import com.flatmate.model.Profile;
import com.flatmate.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    private final ProfileRepository profiles;

    public MatchService(ProfileRepository profiles) {
        this.profiles = profiles;
    }

    public static int score(Profile a, Profile b) {
        double total = 0;

        // 1) Location (25)
        if (same(a.getCity(), b.getCity())) {
            total += same(a.getArea(), b.getArea()) ? 25 : 10;
        }

        // 2) Budget (25)
        double ratio = Math.abs(a.getBudget() - b.getBudget())
                / (double) Math.max(a.getBudget(), b.getBudget());
        total += 25 * Math.max(0, 1 - 2 * ratio);

        // 3) Sone aur uthne ka time (20)
        total += 14 * closeness(a.getSleepHour(), b.getSleepHour());
        total += 6 * closeness(a.getWakeHour(), b.getWakeHour());

        // 4) Safai (15)
        total += 15 * (1 - Math.abs(a.getCleanliness() - b.getCleanliness()) / 4.0);

        // 5) Habits (15): 6 jaanch, dono taraf se
        int ok = 0;
        if (a.isPrefSmokingOk() || !b.isSmoking()) ok++;
        if (b.isPrefSmokingOk() || !a.isSmoking()) ok++;
        if (a.isPrefDrinkingOk() || !b.isDrinking()) ok++;
        if (b.isPrefDrinkingOk() || !a.isDrinking()) ok++;
        if (!a.isPrefVegOnly() || b.isVegetarian()) ok++;
        if (!b.isPrefVegOnly() || a.isVegetarian()) ok++;
        total += 15 * ok / 6.0;

        return (int) Math.round(total);
    }