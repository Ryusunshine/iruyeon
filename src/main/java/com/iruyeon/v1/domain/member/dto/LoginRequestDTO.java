package com.iruyeon.v1.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequestDTO {

    private String email;
    private String password;
    private String socialType;

}
