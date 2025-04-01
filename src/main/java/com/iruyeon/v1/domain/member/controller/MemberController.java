package com.iruyeon.v1.domain.member.controller;

import com.iruyeon.v1.config.response.Response;
import com.iruyeon.v1.domain.common.enums.AuthMember;
import com.iruyeon.v1.domain.member.dto.TokenResponseDTO;
import com.iruyeon.v1.domain.member.service.MemberFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberFacade memberFacade;

    @PostMapping("reissue")
    public ResponseEntity<Response<String>> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        memberFacade.reissueToken(request, response);
        return ResponseEntity.ok(
                Response.of("success"));
    }

    @PostMapping("logout")
    public ResponseEntity<Response<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        memberFacade.logout(request, response);
        return ResponseEntity.ok(
                Response.of("success"));
    }


}
