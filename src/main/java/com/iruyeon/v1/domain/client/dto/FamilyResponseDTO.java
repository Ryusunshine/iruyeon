package com.iruyeon.v1.domain.client.dto;

import com.iruyeon.v1.domain.client.entity.Family;
import com.iruyeon.v1.domain.client.enums.Relationship;
import com.iruyeon.v1.domain.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyResponseDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private int birthYear;
    private String university;
    private String major;
    private String property;
    private String religion;
    private String currentJob;
    private String previousJob;
    private String jobDetail;
    private String info;
    private String homeTown;
    private Gender gender;
    private Relationship relationship;

    public static FamilyResponseDTO of(Family family) {
        return FamilyResponseDTO.builder()
                .name(family.getName())
                .phoneNumber(family.getPhoneNumber())
                .address(family.getAddress())
                .birthYear(family.getBirthYear())
                .university(family.getUniversity())
                .major(family.getMajor())
                .property(family.getProperty())
                .religion(family.getReligion())
                .currentJob(family.getCurrentJob())
                .previousJob(family.getPreviousJob())
                .jobDetail(family.getJobDetail())
                .info(family.getInfo())
                .homeTown(family.getHomeTown())
                .gender(family.getGender())
                .relationship(family.getRelationship())
                .build();
    }
}

