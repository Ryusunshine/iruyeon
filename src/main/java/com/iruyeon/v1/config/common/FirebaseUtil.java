//package com.iruyeon.v1.config.common;
//
//import com.google.firebase.auth.AuthErrorCode;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@Slf4j
//public class FirebaseUtil {
//
//    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
//        boolean checkRevoked = true;
//        try {
//            return FirebaseAuth.getInstance().verifyIdToken(idToken, checkRevoked);
//        } catch (FirebaseAuthException e) {
//            log.error("Error verifying ID token", e);
//            if (e.getAuthErrorCode() == AuthErrorCode.REVOKED_ID_TOKEN) {
//                log.warn("Token has been revoked", e);
//                throw new FirebaseAuthException(e);
//            } else {
//                throw new FirebaseAuthException(e);
//            }
//        }
//    }
//}
