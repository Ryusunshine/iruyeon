package com.iruyeon.v1.domain.s3.service;

import com.iruyeon.v1.domain.s3.dto.S3PresignedURLRequestDTO;
import com.iruyeon.v1.domain.s3.dto.S3PresignedURLResponseDTO;
import com.iruyeon.v1.domain.s3.service.services.GetS3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Facade {

    private final GetS3PresignedUrlService getS3PresignedUrlService;

    public S3PresignedURLResponseDTO getS3PresignedUrlResponse(S3PresignedURLRequestDTO dto) {
        return getS3PresignedUrlService.getS3PresignedUrlResponse(dto);
    }
}
