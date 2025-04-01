package com.iruyeon.v1.config.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private T data;
    private int status;
    private String message;
    private LocalDateTime responseTime;

    public static Response of(String message) {
        return Response.builder()
                .status(200)
                .responseTime(LocalDateTime.now())
                .message(message)
                .build();
    }

    public static <T> Response<T> of(T data, String message) {
        return (Response<T>) Response.builder()
                .status(200)
                .responseTime(LocalDateTime.now())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Response <T> fail(String message) {
        return (Response<T>) Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .responseTime(LocalDateTime.now())
                .message(message)
                .data(Collections.emptyList())
                .build();
    }
}


