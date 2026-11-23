package com.flatmate.controller;

import com.flatmate.dto.Suggestion;
import com.flatmate.security.AuthUser;
import com.flatmate.service.MatchService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchController {

    private final MatchService matches;

    public MatchController(MatchService matches) {
        this.matches = matches;
    }

    @GetMapping("/api/suggestions")
    public List<Suggestion> suggestions(@AuthenticationPrincipal AuthUser me) {
        return matches.suggestions(me.id());
    }
}