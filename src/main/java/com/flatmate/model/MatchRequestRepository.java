package com.flatmate.repository;

import com.flatmate.model.MatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    List<MatchRequest> findByReceiverIdAndStatus(Long receiverId, String status);

    long countBySenderIdAndStatus(Long senderId, String status);

    @Query("select r from MatchRequest r where (r.senderId = :a and r.receiverId = :b) "
            + "or (r.senderId = :b and r.receiverId = :a)")
    List<MatchRequest> between(@Param("a") Long a, @Param("b") Long b);

    @Query("select r from MatchRequest r where r.senderId = :id or r.receiverId = :id")
    List<MatchRequest> involving(@Param("id") Long id);

    @Query("select r from MatchRequest r where r.status = 'ACCEPTED' "
            + "and (r.senderId = :id or r.receiverId = :id)")
    List<MatchRequest> findAccepted(@Param("id") Long id);
}