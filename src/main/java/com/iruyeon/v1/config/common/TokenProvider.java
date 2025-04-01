package com.iruyeon.v1.config.common;

import com.iruyeon.v1.config.common.security.dto.JwtDTO;
import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.domain.token.entity.RefreshToken;
import com.iruyeon.v1.domain.token.repository.RefreshTokenRepository;
import com.iruyeon.v1.domain.member.enums.Role;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.iruyeon.v1.config.response.ErrorCode.*;


@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {

    private static final String ROLE = "role";

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailServiceImpl userDetailService;

    private static final long JWT_EXPIRATION_MS = 1000L * 60 * 400; //1분으로 일단 수정
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; //7일


    public String generateAccessToken(String email, String role) {
        final Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setSubject(email) // 사용자(principal => phoneNumber)
                .claim("role", role) //권한 설정
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    /**
     * Refresh 토큰 생성 및 Redis에 저장
     */
    public String generateRefreshToken(String email, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim(ROLE, role)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByEmail(email);

        // 토큰이 있다면 업데이트, 없다면 추가
        if (refreshTokenEntity == null) refreshTokenEntity = new RefreshToken(email, refreshToken);
        else refreshTokenEntity.update(refreshToken);
        
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    /**
     * Access 토큰 유효성 검증
     */
    public void validAccessToken(String accessToken) {
        validToken(accessToken);
    }

    /**
     * Refresh 토큰 유효성 검증
     */
    public void validRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null) throw new BaseException(REFRESH_INVALID);

        try {
            validToken(refreshToken);
        } catch (Exception e) {
            // 어떤 오류라도 REFRESH_INVALID로 리턴
            throw new BaseException(REFRESH_INVALID);
        } 
    }

    /**
     * JWT 토큰의 유효성 검증
     */
    public void validToken(String token) {
        try {
            Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new BaseException(TOKEN_EXPIRED);
        } catch (MalformedJwtException | SignatureException e) {
            throw new BaseException(TOKEN_INVALID);
        } 
    }

    /**
     * JWT 토큰 기반으로 인증(Authentication) 객체를 생성
     */
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);

        // 단일 메서드로 역할에 따른 유저 로드
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();  // 관리자 계정 ID / 멤버 UID
    }

    /**
     * 토큰에서 클레임(Claim) 정보 조회
     */
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                        .setSigningKey(jwtProperties.getSecretKey())
                        .parseClaimsJws(token)
                        .getBody();
        } catch (Exception e) {
            throw new BaseException(TOKEN_INVALID);
        }
    }

    /**
     * HTTP 요청의 헤더에서 Bearer 토큰을 추출
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(jwtProperties.getToken().getAuthentication().getHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtProperties.getToken().getAuthentication().getPrefix())) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * JWT 토큰에서 Role 추출
     */
    public Role getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        String roleString = claims.get(ROLE, String.class);
        return Role.valueOf(roleString);
    }

    /**
     * JWT 토큰에서 만료 시간 추출
     */
    public long getExpirationFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime();
    }

    /**
     * 토큰을 응답 헤더와 쿠키에 추가
     * @param role Role
     * @param response HttpServletResponse
     */
    public void setTokensInResponse(String email, String role, HttpServletResponse response) {
        String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        // AccessToken을 응답 헤더에 추가
        response.setHeader(jwtProperties.getToken().getAuthentication().getHeader(), jwtProperties.getToken().getAuthentication().getPrefix() + accessToken);

        // RefreshToken을 HTTP-only 쿠키에 추가
        Cookie refreshTokenCookie = new Cookie(jwtProperties.getToken().getCookieNames().getRefresh(), refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }
}
