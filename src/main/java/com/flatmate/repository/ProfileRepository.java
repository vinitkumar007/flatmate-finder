package com.flatmate.repository;

import com.flatmate.model.profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface profileRepository extends JpaRepository<Profile, Long> {
}