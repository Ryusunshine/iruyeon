package com.iruyeon.v1.utils;

import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SessionUtil {
    /**
     * 서버 측에서 HttpSession을 사용하여 세션을 관리합니다.
     * 이 세션은 기본적으로 클라이언트가 서버에 요청을 보낼 때마다 서버에 저장되고, 클라이언트는 세션 ID를 쿠키를 통해 유지합니다.
     */

    private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";
    private static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";

    public static String getLoginMemberId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_MEMBER_ID);
    }
    public static void setLoginMemberId(HttpSession session, String id) {
        session.setAttribute(LOGIN_MEMBER_ID, id);
    }
    public static String getLoginAdminId(HttpSession session) {
        return (String) session.getAttribute(LOGIN_ADMIN_ID);
    }
    public static void setLoginAdminId(HttpSession session, String id) {
        session.setAttribute(LOGIN_ADMIN_ID, id);
    }
    public static void clear(HttpSession session) {
        session.invalidate();
    }
}
