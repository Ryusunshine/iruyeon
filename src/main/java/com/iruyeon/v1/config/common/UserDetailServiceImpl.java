package com.iruyeon.v1.config.common;

import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email);

        // 회원이 없으면 예외를 던짐
        if (member == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // 회원이 존재하면 해당 회원 객체 반환
        return member;
    }
}
