package com.iruyeon.v1.domain.member.service.services;

import com.iruyeon.v1.config.common.JwtProperties;
import com.iruyeon.v1.config.common.TokenProvider;
import com.iruyeon.v1.config.common.UserDetailServiceImpl;
import com.iruyeon.v1.config.common.security.dto.JwtDTO;
import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.domain.token.entity.RefreshToken;
import com.iruyeon.v1.domain.token.repository.RefreshTokenRepository;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.iruyeon.v1.config.response.ErrorCode.CHANGE_ACCESS_TOKEN_WITH_REFRESH_TOKEN;
import static com.iruyeon.v1.config.response.ErrorCode.TOKEN_MISSING;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private static final String PATH = "/";

    private final TokenProvider jwtTokenProvider;
    private final UserDetailServiceImpl userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;


    public void reissueToken(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null) throw new BaseException(CHANGE_ACCESS_TOKEN_WITH_REFRESH_TOKEN);

        // Refresh Token 검증
        jwtTokenProvider.validRefreshToken(refreshToken);

        // 유저 정보 추출
        String email = jwtTokenProvider.getEmail(refreshToken);
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        // 헤더에 Access Token 추가
        response.setHeader(jwtProperties.getToken().getAuthentication().getHeader(), jwtProperties.getToken().getAuthentication().getPrefix() + jwtTokenProvider.generateAccessToken(email, role));

        // 쿠키에 Refresh Token 추가
        Cookie refreshTokenCookie = new Cookie(jwtProperties.getToken().getCookieNames().getRefresh(), jwtTokenProvider.generateRefreshToken(email, role));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath(PATH);
        response.addCookie(refreshTokenCookie);
    }

    // 쿠키에서 Refresh Token 추출
    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtProperties.getToken().getCookieNames().getRefresh().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new BaseException(TOKEN_MISSING);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        String refreshToken = extractRefreshToken(request);

        // 토큰이 유효하지 않거나 없더라도 로그아웃 처리
        try {
//            // fcm 토큰 삭제
//            deleteFCM(refreshToken);

            // Refresh Token 검증
            if (refreshToken == null) throw new BaseException(TOKEN_MISSING);

            // 등록된 Refresh Token 삭제
            RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
            refreshTokenRepository.delete(refreshTokenEntity);

        } catch (Exception ignored) {
        } finally {
            // 쿠키에서 Refresh Token 삭제
            Cookie refreshTokenCookie = new Cookie(jwtProperties.getToken().getCookieNames().getRefresh(), null);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath(PATH);
            refreshTokenCookie.setMaxAge(0); // 쿠키 만료
        }
    }

//    private void deleteFCM(String token) {
//        String identifier = jwtTokenProvider.getIdentifier(token);
//
//        Us member = memberRepository.findByUID(identifier);
//        if (member != null) {
//            deleteFcmService.deleteFcm(member);
//        } else {
//            Admin admin = adminRepository.findById(identifier);
//            if (admin != null) {
//                deleteFcmService.deleteFcm(admin);
//            }
//        }
//    }
}