package com.iruyeon.v1.domain.search.entity;

import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.entity.Family;
import com.iruyeon.v1.domain.client.enums.MaritalStatus;
import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.member.enums.Gender;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "client")
public class ClientDocument {

    @Id
    private String id;

    private String name;
    private String phoneNumber;
    private String address;
    private int birthYear;
    private MaritalStatus maritalStatus;
    private String highSchool;
    private String university;
    private String eduDegree;
    private String major;
    private String property;
    private String religion;
    private String currentJob;
    private String previousJob;
    private String jobDetail;
    private String info;
    private String homeTown;
    private Gender gender;
    private Status status;
    private int ageGapLower;
    private int ageGapUpper;
    private String interest;
    private String idealType;
    private String personality;

    @Field(name = "has_child", type=FieldType.Boolean)
    private Boolean hasChild;

    @Field(type = FieldType.Keyword)
    private List<String> images;

    @Field(type = FieldType.Nested)
    private Set<Family> family;

    @Builder
    public ClientDocument(String id, String name, String phoneNumber, String address, int birthYear, String highSchool,
                  String university, String major, String property, String religion, String currentJob,
                  String previousJob, String jobDetail, String info, String homeTown, Gender gender, Status status,
                  int ageGapLower, int ageGapUpper, String interest, Set<Family> family, MaritalStatus maritalStatus,
                  String idealType, String personality, boolean hasChild, List<String> images, String eduDegree) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthYear = birthYear;
        this.maritalStatus = maritalStatus;
        this.highSchool = highSchool;
        this.university = university;
        this.major = major;
        this.property = property;
        this.religion = religion;
        this.currentJob = currentJob;
        this.previousJob = previousJob;
        this.jobDetail = jobDetail;
        this.info = info;
        this.homeTown = homeTown;
        this.gender = gender;
        this.status = status;
        this.ageGapLower = ageGapLower;
        this.ageGapUpper = ageGapUpper;
        this.interest = interest;
        this.idealType = idealType;
        this.personality = personality;
        this.hasChild = hasChild;
        this.images = images;
        this.family = family;
        this.eduDegree = eduDegree;
    }

    public static ClientDocument entityToDocument(Client client) {
        return ClientDocument.builder()
                .name(client.getName())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .birthYear(client.getBirthYear())
                .highSchool(client.getHighSchool())
                .university(client.getUniversity())
                .major(client.getMajor())
                .property(client.getProperty())
                .currentJob(client.getCurrentJob())
                .previousJob(client.getPreviousJob())
                .maritalStatus(client.getMaritalStatus())
                .jobDetail(client.getJobDetail())
                .info(client.getInfo())
                .homeTown(client.getHomeTown())
                .gender(client.getGender())
                .status(client.getStatus())
                .ageGapLower(client.getAgeGapLower())
                .ageGapUpper(client.getAgeGapHigher())
                .interest(client.getInterest())
                .idealType(client.getIdealType())
                .personality(client.getPersonality())
                .hasChild(client.getHasChild())
                .images(client.getImages())
                .build();
    }
}