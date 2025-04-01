package com.iruyeon.v1.domain.s3.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadImageService {

    private final S3Service s3Service;

    public String uploadImage(MultipartFile file) {
        try {
            return s3Service.uploadImage(file);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
