package com.iruyeon.v1.config.common.security.dto;

import com.iruyeon.v1.domain.member.enums.Role;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class JwtDTO {

    private final String email;
    private final String role;

    /* 역할 정보가 없음 (registerToken) */
    public JwtDTO(String email) {
        this.email = email;
        //임시 권한 부여
        this.role = null;
    }

    /* 역할 정보가 있음 (accessToken) */
    public JwtDTO(String email, String role) {
        this.email = email;
        //임시 권한 부여
        this.role = role;
    }

    public static JwtDTO of(String email, Role role) {
        return JwtDTO.builder()
                .email(email)
                .role(role.toString())
                .build();
    }
}