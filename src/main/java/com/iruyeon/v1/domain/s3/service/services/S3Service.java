package com.iruyeon.v1.domain.s3.service.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;

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
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }


    public static AwsBasicCredentials createCredentials(String accessKey, String secretAccessKey) {
        return AwsBasicCredentials.create(accessKey, secretAccessKey);
    }

    public String uploadImage(MultipartFile file) {
        S3Presigner presigner = createPresigner();
        String contentType = getExtenstion(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = createFileName(file.getOriginalFilename());
        try {
            URL url = getS3UploadURL(presigner, fileName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestMethod("PUT");
            connection.getOutputStream().write(file.getBytes());
            connection.getResponseCode();
            System.out.println("HTTP response code is " + connection.getResponseCode());
        } catch (IOException e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
        return fileName;
    }

    public URL getS3UploadURL(S3Presigner presigner, String fileName) {
        try {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(getExtenstion(fileName))
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) //링크가 유효한 시간
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            URL myURL = presignedRequest.url();
            System.out.println("Presigned URL to upload a file to: " + myURL.toString());
            System.out.println("Which HTTP method needs to be used when uploading a file: " +
                    presignedRequest.httpRequest().method());

            return myURL;

        } catch (S3Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }


    /**
     * 파일 명 앞에 Random UUID 를 붙인 파일명을 반환합니다
     *
     * @param fileName 파일 명
     * @return BaseURL 제외한 디렉토리명 과 UUID 를 합한 최종 파일 경로
     */
    private String createFileName(String fileName) {

        return "images/origin/" + UUID.randomUUID() + getFileExtension(fileName);
    }

    /**
     * 파일 확장자를 가져옵니다
     *
     * @param fileName 파일 명
     * @return 파일 확장자
     */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }

    private String getExtenstion(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}