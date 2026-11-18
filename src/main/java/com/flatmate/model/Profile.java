package com.flatmate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
public class Profile {

    @Id
    private Long userId;

    private String name;
    private int age;
    private String gender;
    private String occupation;
    private String phone;
    private String city;
    private String area;
    private int budget;
}
