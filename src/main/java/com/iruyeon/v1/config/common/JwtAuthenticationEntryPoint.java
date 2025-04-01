package com.iruyeon.v1.config.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.config.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.iruyeon.v1.config.response.ErrorCode.*;

/**
 * 인증 실패 시 처리하는 클래스
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception != null) {
            if (exception.equals(TOKEN_MISSING.name())) {
                setResponse(response, TOKEN_MISSING);
            } else if (exception.equals(TOKEN_EXPIRED.name())) {
                setResponse(response, TOKEN_EXPIRED);
            } else if (exception.equals(TOKEN_INVALID.name())) {
                setResponse(response, TOKEN_INVALID);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode e) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(e.getStatus());

        ErrorResponse errorResponse = ErrorResponse.of(e);
        String result = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(result);
    }
}
