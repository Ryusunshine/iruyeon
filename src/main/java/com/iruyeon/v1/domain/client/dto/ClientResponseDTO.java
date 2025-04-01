package com.iruyeon.v1.domain.client.dto;

import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private int birthYear;
    private int age;
    private String highSchool;
    private String university;
    private String currentJob;
    private String previousJob;
    private String jobDetail;
    private String info;
    private String homeTown;
    private Gender gender;
    private Status status;
    private int ageGapLower;
    private int ageGapHigher;
    private String interest;
    private String idealType;
    private String personality;
    private boolean hasChild;
    private String property;
    private String major;
    private List<String> profileImages;
    private List<FamilyResponseDTO> families;


    public static ClientResponseDTO of(Client client, List<String> profileImages) {
        return ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .birthYear(client.getBirthYear())
                .age(LocalDate.now().getYear() - client.getBirthYear() + 1)
                .highSchool(client.getHighSchool())
                .university(client.getUniversity())
                .previousJob(client.getPreviousJob())
                .currentJob(client.getCurrentJob())
                .jobDetail(client.getJobDetail())
                .info(client.getInfo())
                .homeTown(client.getHomeTown())
                .families(client.getFamily().stream().map(FamilyResponseDTO::of).toList())
                .gender(client.getGender())
                .status(client.getStatus())
                .ageGapLower(LocalDate.now().getYear()  + 1 - client.getAgeGapLower())
                .ageGapHigher(LocalDate.now().getYear()  + 1 + client.getAgeGapHigher())
                .interest(client.getInterest())
                .idealType(client.getIdealType())
                .personality(client.getPersonality())
                .hasChild(client.getHasChild())
                .property(client.getProperty())
                .major(client.getMajor())
                .profileImages(profileImages)
                .build();
    }

}
