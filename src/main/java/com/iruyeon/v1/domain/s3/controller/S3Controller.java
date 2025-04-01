package com.iruyeon.v1.domain.s3.controller;

import com.iruyeon.v1.config.response.Response;
import com.iruyeon.v1.domain.s3.dto.S3PresignedURLRequestDTO;
import com.iruyeon.v1.domain.s3.dto.S3PresignedURLResponseDTO;
import com.iruyeon.v1.domain.s3.service.S3Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Facade s3Facade;

    @PostMapping
    public ResponseEntity<Response<S3PresignedURLResponseDTO>> getS3PresignedUrl(
            @RequestBody S3PresignedURLRequestDTO dto
    ) {
        return ResponseEntity.ok(
                Response.of(
                        s3Facade.getS3PresignedUrlResponse(dto),
                        "success"
                ));
    }
}
