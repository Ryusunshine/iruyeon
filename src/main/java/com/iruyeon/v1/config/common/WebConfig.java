package com.iruyeon.v1.config.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    //WebMvcConfigurer는 Spring MVC의 설정을 커스터마이징할 수 있도록 해주는 인터페이스.
    //addCorsMappings 메서드를 오버라이드하여 CORS 설정을 직접 추가할 수 있음.

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //애플리케이션의 **모든 API 엔드포인트(/**)**에 대해 CORS를 허용
                .allowedOrigins("http://localhost:3000") //어떤 도메인을 허락하것인지
                .allowedMethods("*") //모든 method 허용
                .allowedHeaders("*")
                .allowCredentials(true) // 자격 증명 허용
                .exposedHeaders("Authorization") //Authorizatiion에 토큰값을 세팅해서 보낼건데 이 보안상에 내가 자격이 있다라고 증명하는것이다
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name());
    }
}

//TODO : @Bean 과 @Configuration 차이 공부
