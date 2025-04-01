package com.iruyeon.v1.domain.common.enums;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
*  request 의 Authorization 헤더를 가지고
 * JwtAuthenticationFilter 를 통과하면서
 * SecurityContextHolder 에 로그인 된 유저의 ...
* */

/**
 * UserDetails 의 커스텀 구현체 MemberAdapter 를 가져온 후
 * member 객체 프로퍼티를 추출하는 어노테이션
 * */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthMember {
}
//@Target({ElementType.PARAMETER, ElementType.METHOD})
//이 어노테이션이 어디에 적용될 수 있는지를 정의합니다.
//ElementType.PARAMETER → 메서드의 파라미터에 사용 가능
//ElementType.METHOD → 메서드에도 적용 가능 (하지만 일반적으로 @AuthenticationPrincipal은 파라미터에 사용됨)

//@Retention(RetentionPolicy.RUNTIME)
//이 어노테이션이 런타임에도 유지됨을 의미합니다.
//즉, Reflection API를 통해 실행 중에도 어노테이션을 조회할 수 있습니다.

//@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
//@AuthenticationPrincipal은 Spring Security에서 현재 로그인한 사용자 객체를 가져오는 역할을 합니다.
//expression = "#this == 'anonymousUser' ? null : member":
//Spring Security에서 인증되지 않은 사용자는 "anonymousUser"라는 기본 값으로 설정됩니다.
//따라서 익명 사용자(비로그인 상태)라면 null을 반환하고,
//그렇지 않으면 member 필드를 가져옵니다.

//member가 어디서 오는가?
//현재 로그인한 사용자 객체가 UserDetails 또는 CustomUserDetails를 상속받는 경우, 해당 객체 내부의 member 필드를 가져오는 것임.
