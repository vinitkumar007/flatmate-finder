package com.flatmate.service;

import com.flatmate.dto.MessageView;
import com.flatmate.exception.ApiException;
import com.flatmate.model.Message;
import com.flatmate.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {

    private final MessageRepository messages;
    private final RequestService requests;

    public ChatService(MessageRepository messages, RequestService requests) {
        this.messages = messages;
        this.requests = requests;
    }

    public MessageView send(Long me, Long other, String text) {
        requireConnected(me, other);

        String clean = text.trim();
        if (clean.isEmpty()) {
            throw new ApiException(400, "Khali message nahi bhej sakte");
        }

        Message m = new Message();
        m.setSenderId(me);
        m.setReceiverId(other);
        m.setBody(clean);
        m = messages.save(m);

        return new MessageView(m.getId(), true, m.getBody(), m.getCreatedAt());
    }