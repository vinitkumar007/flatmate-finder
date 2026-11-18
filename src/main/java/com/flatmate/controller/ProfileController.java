package com.flatmate.controller;

import com.flatmate.dto.ProfileRequest;
import com.flatmate.model.Profile;
import com.flatmate.security.AuthUser;
import com.flatmate.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public Profile me(@AuthenticationPrincipal AuthUser me) {
        return service.get(me.id());
    }

    @PutMapping("/me")
    public Profile save(@AuthenticationPrincipal AuthUser me, @Valid @RequestBody ProfileRequest r) {
        return service.save(me.id(), r);
    }
}