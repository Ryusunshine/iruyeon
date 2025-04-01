package com.iruyeon.v1.domain.match.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchRequestDTO {

    private Long fromMemberId;
    private Long toMemberId;
    private Long fromClientId;
    private Long toClientId;
    private String message;
}
