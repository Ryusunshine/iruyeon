package com.iruyeon.v1.config.common;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.member.enums.Role;
import com.iruyeon.v1.domain.member.service.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class  TokenAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter = Spring Security 전용 필터.

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final static String EXCLUDE_PATH = "/api/v0/auth/logout";

    // 전체 흐름 요약
    // 1. 요청 URI 로깅
    // 2. Authorization 헤더에서 JWT 토큰 추출
    // 3. JWT 토큰 검증 및 사용자 인증 정보 설정
    // 4. ContentCachingResponseWrapper로 응답 데이터 래핑
    // 5. 필터 체인의 다음 필터 실행 (filterChain.doFilter)
    // 6. 토큰의 Role과 DB Role 비교
    // 7. Role이 다르면 새로운 JWT 토큰 재발급

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //현재 요청된 URI
        String requestUri = request.getRequestURI();
        log.info("Request URI: {}", requestUri);

        // 토큰 유효성 검사
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // Authorization 헤더에서 Bearer 토큰을 파싱하는 메서드
        String token = getAccessToken(authorizationHeader);

        // 인증 처리
        try {
            if (!requestUri.equals(EXCLUDE_PATH)) {
                // 토큰 존재하면
                if (token != null && !token.trim().isEmpty()) {
                    // 토큰 유효성 검사
                    tokenProvider.validAccessToken(token);
                    // 토큰에서 인증 정보 가져오기
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    // Authentication 객체는 인증 객체이다.
                    // 이걸 감싸고 있는 객체가 Security 객체이고 이걸 감싸고 있는 객체가 SecurityContenxtHolder이다.
                    // 즉 Authentication(인증)객체는 SecurityContenxtHolder 객체안에 들어가있다.
                    // Spring Security의 SecurityContext에 인증 정보 저장
                    //(이후 컨트롤러에서 @AuthenticationPrincipal로 사용자 정보 접근 가능)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new BaseException(ErrorCode.TOKEN_MISSING);
                }
            }
        } catch (BaseException e) {
            SecurityContextHolder.clearContext(); // 보안상 인증 정보 초기화
            request.setAttribute("exception", e.getErrorCode().name());
        }

        // ContentCachingResponseWrapper = HttpServletResponse를 래핑하는 클래스.
        // HttpServletResponse의 getWriter() 또는 getOutputStream()을 한 번 사용하면 이후에는 다시 읽을 수 없어.
        // ContentCachingResponseWrapper는 응답 본문을 캐싱하여 여러 번 읽을 수 있게 해줌.
        //예를 들어, API 응답을 로깅하거나 응답 데이터를 변경하는 경우 유용
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 다음 필터 실행하면서 래핑된 응답 객체(responseWrapper)를 전달.
        // 이 코드가 실행되면 컨트롤러에서 요청을 처리한 후 다시 필터로 돌아옴
        // doFilter 코드는 필터체인으로 돌아가라 라는 뜻
        filterChain.doFilter(request, responseWrapper);

        if (!requestUri.equals(EXCLUDE_PATH)) {
            //SecurityContextHolder에서 현재 사용자 인증 정보 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && token != null && !token.trim().isEmpty()) {
                // 토큰이 여전히 유효한지 검사
                tokenProvider.validAccessToken(token);
                // Authentication에서 현재 로그인한 사용자의 정보(email, name, role) 조회
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                Role principalRole = userPrincipal.getRole();
                // JWT 토큰에서 Role(Role 정보) 가져오기
                Role tokenRole = tokenProvider.getRoleFromToken(token);
            
                // 토큰 Role과 유저의 Role이 일치하지 않을 경우 토큰 재발급
                if (!tokenRole.equals(principalRole)) {
                    log.warn("[Token Role Mismatch] Token Role: {}, DB Role: {}", tokenRole, principalRole);
                    authService.reissueToken(request, responseWrapper);
                }
            }
        }
        // ContentCachingResponseWrapper의 응답 본문을 실제 HttpServletResponse로 복사함
        responseWrapper.copyBodyToResponse();
    }

    private String getAccessToken(String authorizationHeader) {
        // 보통 "Bearer <JWT 토큰>" 형식이므로 "Bearer " 부분을 제거하고 토큰만 추출
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            // TOKEN_PREFIX이후부터 자르기
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}