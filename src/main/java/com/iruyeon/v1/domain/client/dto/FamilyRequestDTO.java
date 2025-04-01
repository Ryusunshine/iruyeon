package com.iruyeon.v1.domain.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FamilyRequestDTO {
    private String name;
    private String phoneNumber;
    private String address;
    private int birthYear;
    private String maritalStatus;
    private String highSchool;
    private String university;
    private String major;
    private String property;
    private String religion;
    private String currentJob;
    private String previousJob;
    private String jobDetail;
    private String info;
    private String homeTown;
    private String gender;
    private String relationShip;
}
