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
        public List<MessageView> conversation(Long me, Long other) {
        requireConnected(me, other);

        List<Message> latest = new ArrayList<>(messages.conversation(me, other, PageRequest.of(0, 100)));
        Collections.reverse(latest);

        List<MessageView> out = new ArrayList<>();
        for (Message m : latest) {
            out.add(new MessageView(m.getId(), m.getSenderId().equals(me), m.getBody(), m.getCreatedAt()));
        }
        return out;
    }

    private void requireConnected(Long me, Long other) {
        if (!requests.areConnected(me, other)) {
            throw new ApiException(403, "Chat sirf accepted connection ke saath ho sakti hai");
        }
    }
}