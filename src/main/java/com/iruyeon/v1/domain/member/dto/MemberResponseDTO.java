package com.iruyeon.v1.domain.member.dto;

import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDTO {

    private final Long id;
    private final String name;
    private final String phoneNumber;
    private final String detail;
    private final Gender gender;
    private final Status status;

    public static MemberResponseDTO of(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .detail(member.getDetail())
                .gender(member.getGender())
                .status(member.getStatus())
                .build();
    }
}
