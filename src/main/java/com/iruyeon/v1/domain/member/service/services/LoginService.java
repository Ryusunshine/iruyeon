package com.iruyeon.v1.domain.member.service.services;

import com.iruyeon.v1.config.common.TokenProvider;
import com.iruyeon.v1.config.common.security.dto.JwtDTO;
import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.member.dto.LoginRequestDTO;
import com.iruyeon.v1.domain.member.dto.TokenResponseDTO;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO dto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(dto.getEmail());
        if (member == null)
            throw new BaseException(ErrorCode.MEMBER_NOT_FOUND);

        if (dto.getSocialType() == null && dto.getPassword() != null
                && !passwordEncoder.matches(dto.getPassword(), member.getPassword()))
            throw new BaseException(ErrorCode.AUTH_UNAUTHORIZED);

        if (dto.getSocialType() == null && dto.getPassword() != null
                && passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            tokenProvider.setTokensInResponse(member.getEmail(), member.getRole().toString(), response);
        }

        return null;
    }
}
