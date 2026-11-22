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
public class profile {

    @Id 
    private Long UserId;

    private String name;
    private String gender;
    private int age;
    private String occupation;
    private String phone;
    private String city;
    private String area;
    private int budget;

    private int sleepHour;
    private int wakeHour;
    private int cleanliness;
    private boolean smoking;
    private boolean drinking;
    private boolean vegetarian;

    private String prefGender;
    private int prefMinAge;
    private int prefMaxAge;
    private boolean prefSmokingOk;
    private boolean prefDrinkingOk;
    private boolean prefVegOnly;

    private boolean visible = true;
    
}
