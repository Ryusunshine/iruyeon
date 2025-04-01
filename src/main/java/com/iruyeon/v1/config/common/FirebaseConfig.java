//package com.iruyeon.v1.config.common;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.messaging.FirebaseMessaging;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Slf4j
//@Configuration
//public class FirebaseConfig {
//
//    @Value("${firebase.config.path}")
//    private String serviceAccountFilePath;
//
//    // 메시징만 권한 설정
//    @Value("${fcm.key.scope}")
//    private String fireBaseScope;
//
//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        InputStream serviceAccountStream;
//
//        if (serviceAccountFilePath.startsWith("static")) {
//            ClassPathResource serviceAccount = new ClassPathResource(serviceAccountFilePath);
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())
//                            .createScoped(fireBaseScope)) // FCM 권한 추가
//                    .build();
//            return FirebaseApp.initializeApp(options);
//        } else {
//            // Assume it's a file path
//            File file = new File(serviceAccountFilePath);
//            serviceAccountStream = new FileInputStream(file);
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
//                    .build();
//            return FirebaseApp.initializeApp(options);
//        }
//    }
//
//    @Bean
//    public FirebaseAuth getFirebaseAuth() {
//        try {
//            return FirebaseAuth.getInstance(firebaseApp());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Bean
//    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }
//
//}