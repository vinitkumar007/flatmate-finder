package com.flatmate.controller;

import com.flatmate.dto.ChatSend;
import com.flatmate.dto.MessageView;
import com.flatmate.security.AuthUser;
import com.flatmate.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chat;

    public ChatController(ChatService chat) {
        this.chat = chat;
    }

    @GetMapping("/{userId}")
    public List<MessageView> conversation(@AuthenticationPrincipal AuthUser me, @PathVariable Long userId) {
        return chat.conversation(me.id(), userId);
    }

    @PostMapping("/{userId}")
    public MessageView send(@AuthenticationPrincipal AuthUser me, @PathVariable Long userId,
                            @Valid @RequestBody ChatSend body) {
        return chat.send(me.id(), userId, body.text());
    }
}