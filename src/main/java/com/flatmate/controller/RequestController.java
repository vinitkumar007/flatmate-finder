package com.flatmate.controller;

import com.flatmate.dto.Connection;
import com.flatmate.dto.RequestView;
import com.flatmate.security.AuthUser;
import com.flatmate.service.RequestService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RequestController {

    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    @PostMapping("/requests/send/{userId}")
    public Map<String, String> send(@AuthenticationPrincipal AuthUser me, @PathVariable Long userId) {
        service.send(me.id(), userId);
        return Map.of("message", "Request bhej di gayi");
    }

    @GetMapping("/requests/incoming")
    public List<RequestView> incoming(@AuthenticationPrincipal AuthUser me) {
        return service.incoming(me.id());
    }

    @PostMapping("/requests/{id}/accept")
    public Map<String, String> accept(@AuthenticationPrincipal AuthUser me, @PathVariable Long id) {
        service.respond(me.id(), id, true);
        return Map.of("message", "Request accept ho gayi");
    }

    @PostMapping("/requests/{id}/reject")
    public Map<String, String> reject(@AuthenticationPrincipal AuthUser me, @PathVariable Long id) {
        service.respond(me.id(), id, false);
        return Map.of("message", "Request reject ho gayi");
    }

    @GetMapping("/connections")
    public List<Connection> connections(@AuthenticationPrincipal AuthUser me) {
        return service.connections(me.id());
    }
}