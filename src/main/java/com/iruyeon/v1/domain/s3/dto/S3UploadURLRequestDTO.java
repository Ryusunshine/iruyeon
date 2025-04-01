package com.iruyeon.v1.domain.s3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class S3UploadURLRequestDTO {
    private List<String> urlList; // [image1, null, image3, null]

}
