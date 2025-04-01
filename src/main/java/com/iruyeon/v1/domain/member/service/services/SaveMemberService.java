package com.iruyeon.v1.domain.member.service.services;

import com.iruyeon.v1.domain.member.dto.MemberRequestDTO;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.enums.Gender;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveMemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member saveMember(MemberRequestDTO dto) {
        return memberRepository.save(
                Member.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .phoneNumber(dto.getPhone())
                        .gender(Gender.valueOf(dto.getGender()))
                        .build());
    }
}
