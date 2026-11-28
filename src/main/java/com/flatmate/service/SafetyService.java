package com.flatmate.service;

import com.flatmate.exception.ApiException;
import com.flatmate.model.Block;
import com.flatmate.model.Report;
import com.flatmate.repository.BlockRepository;
import com.flatmate.repository.ReportRepository;
import com.flatmate.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SafetyService {

    private final BlockRepository blocks;
    private final ReportRepository reports;
    private final UserRepository users;

    public SafetyService(BlockRepository blocks, ReportRepository reports, UserRepository users) {
        this.blocks = blocks;
        this.reports = reports;
        this.users = users;
    }

    @Transactional
    public void block(Long me, Long target) {
        if (me.equals(target)) {
            throw new ApiException(400, "Khud ko block nahi kar sakte");
        }
        users.findById(target).orElseThrow(() -> new ApiException(404, "User nahi mila"));

        if (blocks.existsBetween(me, target)) {
            return;
        }
        Block b = new Block();
        b.setBlockerId(me);
        b.setBlockedId(target);
        blocks.save(b);
    }

    @Transactional
    public void report(Long me, Long target, String reason) {
        if (me.equals(target)) {
            throw new ApiException(400, "Khud ko report nahi kar sakte");
        }
        users.findById(target).orElseThrow(() -> new ApiException(404, "User nahi mila"));

        Report r = new Report();
        r.setReporterId(me);
        r.setReportedId(target);
        r.setReason(reason.trim());
        reports.save(r);
    }
}