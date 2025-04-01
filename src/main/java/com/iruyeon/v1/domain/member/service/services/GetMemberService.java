package com.iruyeon.v1.domain.member.service.services;

import com.iruyeon.v1.config.common.OAuth2Attributes;
import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.member.dto.MemberRequestDTO;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.enums.Role;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member getMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    @Transactional
    public Member getOrSaveMember(String email, String name, String registerId) {
        Member member = memberRepository.findByEmail(email);
        try {
            if (member != null) {
                member.updateName(name);

            } else {
                member = memberRepository.save(Member.builder()
                        .email(email)
                        .name(name)
                        .socialId(registerId)
                        .role(Role.ANONYMOUS)
                        .build());
            }
            return member;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
