package com.flatmate.repository;

import com.flatmate.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("select p from Profile p where p.userId <> :me and p.visible = true and p.city = :city "
            + "and exists (select u.id from User u where u.id = p.userId and u.verified = true and u.banned = false)")
    List<Profile> findCandidates(@Param("me") Long me, @Param("city") String city);
}