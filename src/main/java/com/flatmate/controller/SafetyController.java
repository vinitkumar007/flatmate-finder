package com.flatmate.controller;

import com.flatmate.dto.ReportRequest;
import com.flatmate.security.AuthUser;
import com.flatmate.service.SafetyService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SafetyController {

    private final SafetyService service;

    public SafetyController(SafetyService service) {
        this.service = service;
    }

    @PostMapping("/block/{userId}")
    public Map<String, String> block(@AuthenticationPrincipal AuthUser me, @PathVariable Long userId) {
        service.block(me.id(), userId);
        return Map.of("message", "User block ho gaya");
    }

    @PostMapping("/report/{userId}")
    public Map<String, String> report(@AuthenticationPrincipal AuthUser me, @PathVariable Long userId,
                                      @Valid @RequestBody ReportRequest r) {
        service.report(me.id(), userId, r.reason());
        return Map.of("message", "Report bhej di gayi. Admin check karega.");
    }
}