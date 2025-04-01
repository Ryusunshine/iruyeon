package com.iruyeon.v1.domain.member.entity;

import com.iruyeon.v1.config.common.UserPrincipal;
import com.iruyeon.v1.domain.common.BaseEntity;
import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.member.enums.Gender;
import com.iruyeon.v1.domain.member.enums.Role;
import com.iruyeon.v1.domain.member.enums.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_member")
public class Member extends BaseEntity implements UserPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    private String detail; // 어느 소속

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String image;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    @Builder
    public Member(String email, String password, String name, String phoneNumber, Gender gender, String detail,
                  SocialType socialType, String socialId, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.status = Status.YET_APPROVED;
        this.detail = detail;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 토큰을 만들때 claims에 role를 넣는다.
        // user 또는 admin이 들어가있을것인데 그 role를 authority 객체에 세팅해줄것이다.

        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void updateName(String newName) {
        this.name = newName;
    }

}
