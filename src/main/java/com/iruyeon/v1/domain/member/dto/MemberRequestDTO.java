package com.iruyeon.v1.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberRequestDTO {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String socialId;
    private String socialType;

}
