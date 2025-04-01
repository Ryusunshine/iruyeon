package com.iruyeon.v1.config.common;

import com.iruyeon.v1.domain.member.repository.MemberRepository;
import com.iruyeon.v1.domain.member.service.services.GetMemberService;
import com.iruyeon.v1.domain.token.repository.RefreshTokenRepository;
import com.iruyeon.v1.domain.member.service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final JwtProperties jwtProperties;
    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final SecurityRoleProperties securityRoleProperties;
    private final PublicAccessRequestMatcherUtil publicAccessRequestMatcherUtil;
    private final GetMemberService getMemberService;

    private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:3000",
            "https://knockdog.net",
            "https://www.knockdog.net",
            "https://zemt4mu2f2.execute-api.ap-northeast-2.amazonaws.com"  // API Gateway URL
    );

    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS");
    private static final List<String> ALLOWED_HEADERS = List.of("*");
    private static final String EXPOSED_HEADERS = "Authorization";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //CORS는 프론트엔드 화면이 있을떄 의미가 있는 설정
                //같은 도메인끼리만 api를 통해서 데이터를 주고받겠다 라는 설정이다
                //서버는 8000번이고 프론트엔트 로컬호스트는 3000번으로 설정을 할거라서
                // 두개의 도메인이 맞지 않으므로 예외적으로 3000번은 내가 허용하겟다라는 예외 설정을 추가한다.

                .csrf(AbstractHttpConfigurer::disable)
                //csrf는 보안 공격 중 하나인데 보통 mvc 패턴에서 많이 사용되는 공격이다. 하지만 mvc 패턴이 아니고 서버는 restfull 하게 설계가 되어있고
                // 프론트엔드가 별도로 따로 되어있는 아키텍처다 보니깐 csrf 라고 하는 보안 공격에 대해서 별도로 활성화 시키지 않는 설정이다.

                .httpBasic(AbstractHttpConfigurer::disable)
                // 사용자 이름과 비밀번호를 Base64로 인코딩하여 인증값으로 활용
                // Base64는 암호화가 아니라 인코딩이다. 그래서 httpBasic방식은 사용하지 않고 토큰 방식(토큰의 signature 부분에 암호화)사용할것이다.

                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(this::configureAuthorization)
                //위의 url들을 UsernamePasswordAuthenticationFilter.class(폼 로그인 처리하는 클래스) 검증하기전에
                // tokenAuthenticationFilter에서 검증하겠다라고 설정
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .oauth2Login(oauth2 -> oauth2
                        //authorizationEndpoint: 로그인 요청을 보낼 때 필요한 설정을 담당
                        // OAuth2AuthorizationRequestBasedOnCookieRepository를 사용하여 인증 요청을 쿠키에 저장하고 관리한다.
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        //oAuth2UserCustomService는 로그인 후 외부 인증 제공자(예: 구글, 페이스북 등)로부터 사용자의 정보를 받아와서
                        // 애플리케이션의 사용자 정보로 변환하는 역할을 합니다.
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService))
                        .successHandler(oAuth2SuccessHandler()))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")))
                .build();
    }

    public void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                // 특정 url 패턴에 대해서는 인증처리(Authentication객체 생성)제외
                // 로그인 예외처리할 엔드포인트
                // 사용자가 보내온 토큰과 내가 만들어준 토큰이 맞는지 아닌지 시크릿키값을 활용해서 검증을 해봐야한다.
                // 검증에서 성공하고나면 서버에서는 서버의 메모리값으로 메모리에 Authentication이라는 객체를 만드는데 이게 만들어지면
                // 스프링 시큐리티 의존성에서 사용자가 로그인에 성공했구나 라고 인지하게 된다.
                // 특정 url 에 대해서는 authentication 객체 안만들어줘도 된다라고 명시하는것이다.
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**", "/image/**").permitAll() // resizing을 위한 임시 image/**
                .requestMatchers(
                        "/api/v0/auth/device",
                        "/api/v0/auth/reissue",
                        "/api/v0/auth/logout",
                        "/api/client/search/**",
                        "/client/upload"

                ).permitAll()
                .requestMatchers(publicAccessRequestMatcherUtil).permitAll() // 여기에 publicAccess 어노테이션이 붙은 컨트롤러 메소드는 permitAll
                .requestMatchers("/api/v0/admin/attendance/dog/info/record", "/api/v0/member/main/album").hasAnyRole(securityRoleProperties.getApprovedRoles())
                .requestMatchers("/api/v0/member/**").hasAnyRole(securityRoleProperties.getMemberRoles())
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setExposedHeaders(List.of(EXPOSED_HEADERS));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(jwtProperties, tokenProvider, refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(), getMemberService);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, authService);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}