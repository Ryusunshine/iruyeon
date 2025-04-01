package com.iruyeon.v1.aop;

import com.iruyeon.v1.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
public class LoginCheckAspect {

    @Around("@annotation(com.iruyeon.v1.aop.LoginCheck) && @ annotation(logincheck)")
    public Object loginCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = (HttpSession)((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        String id = null;
        int idIndex = 0;
        String userType = loginCheck.type().toString();
        switch (userType) {
            case "ADMIN":
                id = SessionUtil.getLoginAdminId(session);
                break;
            case "USER":
                id = SessionUtil.getLoginMemberId(session);
                break;
        }
        if (id == null) {
            log.error(proceedingJoinPoint.toString() + "accountName : " + id);
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요.") {};
        }

        // 로그인 성공한 경우
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();
        modifiedArgs[idIndex] = id;
        return proceedingJoinPoint.proceed(modifiedArgs);
    }
}
