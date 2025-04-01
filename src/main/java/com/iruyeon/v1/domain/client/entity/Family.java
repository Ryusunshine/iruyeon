package com.iruyeon.v1.domain.client.entity;

import com.iruyeon.v1.domain.client.enums.Relationship;
import com.iruyeon.v1.domain.member.enums.Gender;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;

@Embeddable //@Embeddable을 사용하여 값 타입 객체임을 정의
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//외부에서 직접 객체 생성을 막을 수 있음
//public으로 하면 누구나 new Family()를 호출할 수 있음 → 불변성 깨질 가능성 O
//protected로 하면 JPA는 사용할 수 있지만, 외부에서는 new Family()를 직접 호출하지 못함.
public class Family {

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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Relationship relationship; // 관계

    @Builder
    public Family(String name, String phoneNumber, String address, int birthYear, String university,
                  String major, String property, String religion, String currentJob, String previousJob, String jobDetail,
                  String info, String homeTown, Gender gender, Relationship relationship) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthYear = birthYear;
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
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Family family = (Family) o;
        return Objects.equals(name, family.name) &&
                Objects.equals(phoneNumber, family.phoneNumber); // 예시: 중요한 필드만 비교
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber); // 중요 필드만 해시 코드 계산
    }
}
