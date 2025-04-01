package com.iruyeon.v1.config.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Parameter Error
    PARAMETER_MISSING(400, "PARAM-400-1", "필수 파라미터가 누락되었습니다."),

    // Image Error
    IMAGE_NOT_FOUND(404, "IMAGE-404-1", "해당 이미지를 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAILED(500, "IMAGE-500-1", "이미지 업로드에 실패했습니다."),

    // User Error
    MEMBER_NOT_FOUND(404, "USER-404-1", "해당 유저를 찾을 수 없습니다."),
    LOGIN_FAILED(401, "AUTH-401-2", "로그인에 실패했습니다"),
    USER_NOT_ACTIVE(404, "MEMBER-404-3", "해당 회원이 유효하지 않습니다"),

    // Match Error
    MATCH_NOT_FOUND(404, "MATCH-404-1", "해당 매칭 정보를 찾을 수 없습니다."),

    // Member Error,
    MEMBER_PROFILE_NOT_FOUND(404, "MEMBER-404-1", "해당 회원이 존재하지 않습니다."),
    MEMBER_PROFILE_NOT_OWNER(404, "MEMBER-404-2", "해당 멤버는 소유자가 아닙니다."),
    MEMBER_NOT_ACTIVE(404, "MEMBER-404-3", "해당 회원이 유효하지 않습니다"),

    // Admin Error
    ADMIN_NOT_FOUND_ID(401, "ADMIN-404-1", "해당 ID를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(404, "ADMIN-404-2", "관리자가 존재하지 않습니다."),
    ADMIN_ID_EXISTS(409, "ADMIN-409-1", "이미 사용중인 아이디입니다."),
    ADMIN_EXISTS(409, "ADMIN-409-2", "이미 등록된 정보입니다."),

    // Auth Error
    AUTH_UNAUTHORIZED(401, "AUTH-401-1", "비밀번호가 일치하지 않습니다"),
    AUTH_DENIED(401, "AUTH-401-2", "승인이 거부되었습니다."),
    AUTH_PENDING(401, "AUTH-401-3", "승인 대기중입니다."),
    AUTH_FAILED(401, "AUTH-401-4", "인증에 실패했습니다."),
    AUTH_FORBIDDEN(403, "AUTH-403-1", "관리자만 접근가능합니다."),

    // S3
    AMAZON_ACCESS_DENIED(403, "C009", "Amazon S3 접근이 거부되었습니다"),

    // JWT Token Error
    TOKEN_INVALID(401, "TOKEN-401-1", "유효하지 않은 액세스 토큰입니다."),
    TOKEN_EXPIRED(401, "TOKEN-401-2", "액세스 토큰이 만료되었습니다."),
    TOKEN_MISSING(401, "TOKEN-401-3", "액세스 토큰이 누락되었습니다."),
    REFRESH_INVALID(401, "TOKEN-401-4", "유효하지 않은 리프레시 토큰입니다."),
    CHANGE_ACCESS_TOKEN_WITH_REFRESH_TOKEN(401, "TOKEN-401-5", "리프레쉬 토큰을 사용하여 액세스 토큰을 변경해주세요."),

    // Firebase Token Error
    FIREBASE_TOKEN_EXPIRED(401, "FIREBASE-401-1", "제공된 Firebase ID 토큰이 만료되었습니다."),
    FIREBASE_TOKEN_REVOKED(401, "FIREBASE-401-2", "Firebase ID 토큰이 취소되었습니다."),

    // 일반적인 에러
    COMMON_BAD_REQUEST(400, "COMMON-400-1", "요청한 값이 올바르지 않습니다"),
    COMMON_CONFLICT(409, "COMMON-409-1", "요청한 값을 처리할 수 없습니다"),
    COMMON_NOT_FOUND(404, "COMMON-404-1", "해당 정보가 없습니다."),
    COMMON_METHOD_NOT_ALLOWED(405, "COMMON-405-1", "허용되지 않은 메소드 입니다"),
    COMMON_INTERNAL_SERVER_ERROR(500, "COMMON-500-1", "일시적인 서버 오류 입니다"),

    // REST ERROR
    REST_SERVER_ERROR(500, "REST-500-1", "통신 서버 오류입니다");

    private final int status;
    private final String code;
    private final String message;

    public String getErrorMessage(Object... arg) {
        return String.format(message, arg);
    }
}
