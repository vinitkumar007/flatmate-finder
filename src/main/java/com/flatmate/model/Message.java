package com.flatmate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity 
@Table(name = "messages")
@Getter 
@Setter 
public class Message {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long reciverId;

    @Column(nullable = false, length = 500)
    private String body;

    private Instant createdAt = Instant.now();
    

}