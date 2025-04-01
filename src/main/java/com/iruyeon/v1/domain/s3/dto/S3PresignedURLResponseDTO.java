package com.iruyeon.v1.domain.s3.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class S3PresignedURLResponseDTO{ //s3 presignUrl
    private List<String> urlList;

    public static S3PresignedURLResponseDTO of(List<String> urlList) {
        return S3PresignedURLResponseDTO.builder()
                .urlList(urlList)
                .build();
    }
}
