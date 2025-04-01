package com.iruyeon.v1.domain.member.service;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.domain.member.service.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final AuthService authService;

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        authService.reissueToken(request, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        authService.logout(request, response);
    }
}
