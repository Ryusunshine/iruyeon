package com.iruyeon.v1.domain.s3.dto;

import com.iruyeon.v1.domain.s3.enums.AwsS3Directory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class S3PresignedURLRequestDTO { // s3 presignUrl
    private Long memberProfileId;
    private Long userId;
    private AwsS3Directory dirName;
    private List<String> contentTypeList;
}
