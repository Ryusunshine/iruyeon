package com.iruyeon.v1.config.common;

import com.iruyeon.v1.config.common.annotation.PublicAccess;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Component //Spring의 컴포넌트로 등록되어 Spring 컨텍스트에서 자동으로 관리된다.
@RequiredArgsConstructor
public class PublicAccessRequestMatcherUtil implements RequestMatcher {
	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	// 이 클래스는 Spring Security의 RequestMatcher를 구현하여 요청이 @PublicAccess 어노테이션이 있는 메서드를 처리하는 경우를 구분합니다.
	// matches 메서드는 요청에 대해 처리할 핸들러를 찾고, 해당 핸들러가 @PublicAccess 어노테이션을 가지고 있는지 확인하여 퍼블릭 요청인지 여부를 판단합니다.
	//이 클래스를 사용하면 @PublicAccess 어노테이션이 붙은 메서드에 대해서만 특정 보안 설정을 예외적으로 처리할 수 있습니다.
	@Override
	public boolean matches(HttpServletRequest request) {
		try {

			// BEST_MATCHING_HANDLER_ATTRIBUTE가 없을 경우에만 요청 매핑 정보를 가져온다.
			if (request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE) == null) {
				HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
				if (handler != null) {
					request.setAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, handler.getHandler());
				}
			}
			//HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE
			// 이 속성은 요청을 처리할 수 있는 핸들러(메서드)가 이미 매핑되어 있는지 확인하는데 사용됩니다.
			// 요청이 처음 처리될 때, getHandler 메서드를 호출하여 현재 요청을 처리하는 핸들러를 가져옵니다.
			// 이 핸들러 정보는 나중에 사용하기 위해 request 객체에 저장됩니다.

			// 퍼블릭 어노테이션 체크
			Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
			if (handler instanceof HandlerMethod) {
				return ((HandlerMethod) handler).hasMethodAnnotation(PublicAccess.class);
			}
			// 핸들러가 HandlerMethod 객체인 경우, hasMethodAnnotation(PublicAccess.class) 메서드를 호출하여
			// 해당 메서드에 @PublicAccess 어노테이션이 있는지 확인합니다.
			// @PublicAccess 어노테이션이 존재하면 true를 반환하고, 없으면 false를 반환합니다.
		} catch (Exception e) {
			log.error("RequestMatcherUtil error", e);
		}
		return false;
		// @PublicAccess 어노테이션이 없으면 false를 반환하여 요청이 퍼블릭 액세스를 위한 것이 아님을 나타냅니다.
	}
}

