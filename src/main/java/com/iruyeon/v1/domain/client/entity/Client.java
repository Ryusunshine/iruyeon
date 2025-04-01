package com.iruyeon.v1.domain.client.entity;

import com.iruyeon.v1.config.common.StringListConverter;
import com.iruyeon.v1.domain.client.dto.ClientRequestDTO;
import com.iruyeon.v1.domain.client.enums.MaritalStatus;
import com.iruyeon.v1.domain.common.BaseEntity;
import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_client")
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phoneNumber;

    private String address;

    private int birthYear;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private String highSchool;

    private String university;

    private String eduDegree;

    private String major;

    private String property; //재산

    private String religion; // 종교

    private String currentJob;

    private String previousJob;

    private String jobDetail;

    private String info; //기타 특이사항

    private String homeTown;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int ageGapLower; // 선호 나이 차이

    private int ageGapHigher;

    private String interest; // 취미

    private String idealType; // 이상형

    private String personality; // 성격

    private Boolean hasChild; // 출산 여부

    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tb_member_id_fk")
    private Member member; // 담당 매니저

    @ElementCollection
    @CollectionTable(name = "family", joinColumns = @JoinColumn(name = "member_id_fk"))
    private Set<Family> family = new HashSet<>(); // 가족
    //@Embedded가 List<>에 사용될 수 없는 이유
    //JPA는 컬렉션 값 타입을 별도 테이블에서 관리하기 때문
    //@Embedded는 단일 값 필드에서만 사용 가능해.
    //하지만 List<Family> 같은 컬렉션 값 타입은 한 개의 컬럼이 아니라 여러 개의 값이 들어가야 해.
    //JPA 스펙상 @Embedded는 리스트 타입을 지원하지 않음
    //대신 @ElementCollection을 사용해서 별도의 테이블을 생성해야 해.

    //@ElementCollection
    //@ElementCollection은 JPA에서 값 타입 컬렉션을 나타낼 때 사용해.
    //List<>, Set<>, Map<> 같은 컬렉션 타입의 값들을 엔티티가 아닌 별도 테이블에서 관리할 수 있게 해줘.
    //기본적으로 LAZY(지연 로딩)로 동작함.
    //@ElementCollectiond을 쓰면 JPA가 families를 관리할 수 있지만, 테이블 설정을 추가해야 해.

    //@CollectionTable
    //@CollectionTable은 컬렉션 값 타입을 저장할 별도의 테이블을 지정하는 어노테이션이야.
    //name = "member_family"
    //→ 컬렉션 값들을 저장할 테이블 이름을 member_family로 지정.
    //joinColumns = @JoinColumn(name = "member_id")
    //→ member_family 테이블과 Member 엔티티를 연결하는 외래 키(member_id)를 설정.

    //@JoinColumn(name = "tb_family_id_fk")는 현재 엔티티(Client)에 FK를 둔다는 의미인데,
    // mappedBy가 있으면 Hibernate가 자동으로 FK를 반대쪽에서 관리하도록 설정돼.
    //즉, @OneToMany에서 mappedBy를 쓰면 반대 엔티티(Family)가 FK를 관리하므로 @JoinColumn을 사용하면 안 됨.


    @Builder
    public Client(Long id, String name, String phoneNumber, String address, int birthYear, String highSchool,
                         String university, String major, String property, String religion, String currentJob,
                         String previousJob, String jobDetail, String info, String homeTown, Gender gender, Status status,
                         int ageGapLower, int ageGapHigher, String interest, Set<Family> family, MaritalStatus maritalStatus,
                         String idealType, String personality, boolean hasChild, List<String> images, Member member, String eduDegree) {
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
        this.ageGapHigher = ageGapHigher;
        this.interest = interest;
        this.idealType = idealType;
        this.personality = personality;
        this.hasChild = hasChild;
        this.images = images;
        this.member = member;
        this.family = family;
        this.eduDegree = eduDegree;
    }

    public void update(ClientRequestDTO dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.address = dto.getAddress();
        this.birthYear = dto.getBirthYear();
        this.highSchool = dto.getHighSchool();
        this.university = dto.getUniversity();
        this.major = dto.getMajor();
        this.property = dto.getProperty();
        this.currentJob = dto.getCurrentJob();
        this.previousJob = dto.getPreviousJob();
        this.maritalStatus = MaritalStatus.valueOf(dto.getMaritalStatus());
        this.jobDetail = dto.getJobDetail();
        this.info = dto.getInfo();
        this.homeTown = dto.getHomeTown();
        this.gender = Gender.valueOf(dto.getGender());
        this.status = Status.valueOf(dto.getStatus());
        this.ageGapLower = dto.getAgeGapLower();
        this.ageGapHigher = dto.getAgeGapHigher();
        this.interest = dto.getInterest();
        this.idealType = dto.getIdealType();
        this.personality = dto.getPersonality();
        this.hasChild = dto.getHasChild();
        this.images = dto.getImages();
    }

    public void updateImage(List<String> imageList) {
        this.images = imageList;
    }

    public void updateFamily(Set<Family> newFamily) {
        this.family.addAll(newFamily); // 새로운 데이터 추가
    }
}
