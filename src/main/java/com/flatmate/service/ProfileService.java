package com.flatmate.service;

import com.flatmate.dto.ProfileRequest;
import com.flatmate.exception.ApiException;
import com.flatmate.model.Profile;
import com.flatmate.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profiles;

    public ProfileService(ProfileRepository profiles) {
        this.profiles = profiles;
    }

    public Profile get(Long userId) {
        return profiles.findById(userId)
                .orElseThrow(() -> new ApiException(404, "Profile abhi nahi bana"));
    }

    @Transactional
    public Profile save(Long userId, ProfileRequest r) {
        Profile p = profiles.findById(userId).orElseGet(Profile::new);

        p.setUserId(userId);
        p.setName(r.name().trim());
        p.setAge(r.age());
        p.setGender(r.gender());
        p.setOccupation(r.occupation().trim());
        p.setPhone(r.phone().trim());
        p.setCity(r.city().trim().toLowerCase());
        p.setArea(r.area().trim().toLowerCase());
        p.setBudget(r.budget());

        return profiles.save(p);
    }
}