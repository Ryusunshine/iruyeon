package com.iruyeon.v1.domain.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ClientRequestDTO {

    private Long memberProfileId;
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
    private String status;
    private int ageGapLower;
    private int ageGapHigher;
    private String interest;
    private String idealType;
    private String personality;
    private Boolean hasChild;
    private List<String> images;
    private List<FamilyRequestDTO> familyRequestDTOList;

}
