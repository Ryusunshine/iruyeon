package com.iruyeon.v1.config.common;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.service.services.GetMemberService;
import com.iruyeon.v1.domain.token.entity.RefreshToken;
import com.iruyeon.v1.domain.token.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProperties jwtProperties;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final GetMemberService getMemberService;

    /**
     * 소셜 로그인 성공시 jwt 토큰 발급하는 로직
     */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email;
        String name = (String) oAuth2User.getAttributes().get("name");
        log.info("Oauth 로그인 성공 : {}", name);

        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        if ("google".equals(registrationId)) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> responseAttributes = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) responseAttributes.get("email");
        } else {
            throw new IllegalArgumentException("지원되지 않는 인증 제공자입니다.");
        }
        clearAuthenticationAttributes(request, response);

        Member member = getMemberService.getMember(email);

        String refreshToken = tokenProvider.generateAccessToken(email,member.getRole().toString()) ;
        String accessToken = tokenProvider.generateAccessToken(email, member.getRole().toString());
        saveRefreshToken(email, refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);
        addAccessTokenToCookie(request, response, accessToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader(jwtProperties.getToken().getCookieNames().getAccess(), accessToken);
        response.setHeader(jwtProperties.getToken().getCookieNames().getRefresh(), refreshToken);

        // 전달할 데이터를 Map에 담는다.
        String targetUrl = getTargetUrl(accessToken, registrationId, member);

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void saveRefreshToken(String email, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email);
        try {
            refreshToken.update(newRefreshToken);

        } catch (Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) (jwtProperties.getToken().getDurations().getRefresh() / 1000);

        CookieUtil.deleteCookie(request, response, jwtProperties.getToken().getCookieNames().getRefresh());
        CookieUtil.addCookie(response, jwtProperties.getToken().getCookieNames().getRefresh(), refreshToken, cookieMaxAge);
    }

    private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        int cookieMaxAge = (int) (jwtProperties.getToken().getDurations().getAccess() / 1000);

        Cookie accessTokenCookie = new Cookie(jwtProperties.getToken().getCookieNames().getAccess(), accessToken);
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(true); // HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(cookieMaxAge);

        CookieUtil.deleteCookie(request, response, jwtProperties.getToken().getCookieNames().getAccess());
        response.addCookie(accessTokenCookie);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl(String token, String provider, Member member) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:3000/member/oauth/login/code/" + provider + "?")
                .queryParam("token", token)
                .queryParam("id", member.getId())
                .queryParam("role", member.getRole());
        return builder.build().toUriString();
    }

}