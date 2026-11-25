package com.flatmate.service;

import com.flatmate.dto.Connection;
import com.flatmate.dto.RequestView;
import com.flatmate.exception.ApiException;
import com.flatmate.model.MatchRequest;
import com.flatmate.model.Profile;
import com.flatmate.repository.MatchRequestRepository;
import com.flatmate.repository.ProfileRepository;
import com.flatmate.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private static final int MAX_PENDING = 20;

    private final MatchRequestRepository requests;
    private final ProfileRepository profiles;
    private final UserRepository users;

    public RequestService(MatchRequestRepository requests, ProfileRepository profiles, UserRepository users) {
        this.requests = requests;
        this.profiles = profiles;
        this.users = users;
    }

    @Transactional
    public void send(Long me, Long receiverId) {
        if (me.equals(receiverId)) {
            throw new ApiException(400, "Khud ko request nahi bhej sakte");
        }
        if (profiles.findById(me).isEmpty()) {
            throw new ApiException(400, "Pehle apna profile complete karo");
        }

        users.findById(receiverId)
                .filter(u -> u.isVerified() && !u.isBanned())
                .orElseThrow(() -> new ApiException(404, "User nahi mila"));

        if (!requests.between(me, receiverId).isEmpty()) {
            throw new ApiException(409, "Request pehle se maujood hai");
        }
        if (requests.countBySenderIdAndStatus(me, "PENDING") >= MAX_PENDING) {
            throw new ApiException(429, "Bahut saari pending requests hain. Pehle unke jawab ka wait karo.");
        }

        MatchRequest r = new MatchRequest();
        r.setSenderId(me);
        r.setReceiverId(receiverId);
        requests.save(r);
    }