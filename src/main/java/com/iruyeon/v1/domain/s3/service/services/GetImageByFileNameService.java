package com.iruyeon.v1.domain.s3.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetImageByFileNameService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;


    public String getImageByFileName(String fileName) {
        fileName = changeFileNameToResize(fileName);
        try {
            if (fileName.isBlank()) return null;
            if(fileName.contains("http")) return fileName;
            S3Presigner presigner = createPresigner();
            String url = getS3DownloadURL(presigner, fileName);
            presigner.close();
            return url;
        } catch (Exception e) {
            log.error("getImageByFileName - {}", e.getMessage());
            return null;
        }
    }

    private String changeFileNameToResize(String fileName) {
        return fileName.replace("origin", "resized");
    }

    public S3Presigner createPresigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(createCredentials(accessKey, secretAccessKey)))
                .build();
    }

    public String getS3DownloadURL(S3Presigner presigner, String keyName) {
        try {
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(keyName)
                            .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(1440))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest =
                    presigner.presignGetObject(getObjectPresignRequest);

            return presignedGetObjectRequest.url().toString();
        } catch (S3Exception e) {
            throw new BaseException(ErrorCode.AMAZON_ACCESS_DENIED);
        }
    }

    private static AwsBasicCredentials createCredentials(String accessKey, String secretAccessKey) {
        return AwsBasicCredentials.create(accessKey,secretAccessKey);
    }
}

