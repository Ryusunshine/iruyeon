package com.iruyeon.v1.domain.member.dto;

import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDTO {

    private final Long id;
    private final String accessToken;
    private final String refreshToken;
    private final Status status;

    public static TokenResponseDTO of(Member member, String accessToken, String refreshToken, Status status) {
        return TokenResponseDTO.builder()
                .id(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(member.getStatus())
                .build();
    }
}
