package com.iruyeon.v1.domain.s3.service.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.iruyeon.v1.domain.s3.dto.S3PresignedURLRequestDTO;
import com.iruyeon.v1.domain.s3.dto.S3PresignedURLResponseDTO;
import com.iruyeon.v1.domain.s3.enums.AwsS3Directory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Service
@RequiredArgsConstructor
public class GetS3PresignedUrlService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * presigned url을 생성해 주는 메소드. bucket v3에 생성해 줌
     * @return upload url, public url
     */
    public S3PresignedURLResponseDTO getS3PresignedUrlResponse(S3PresignedURLRequestDTO dto) {
        List<String> urlList = generateUploadUrls(dto);

        return S3PresignedURLResponseDTO.of(urlList);
    }

    public S3Presigner createPresigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(createCredentials(accessKey, secretAccessKey)))
                .build();
    }

    private static AwsBasicCredentials createCredentials(String accessKey, String secretAccessKey) {
        return AwsBasicCredentials.create(accessKey,secretAccessKey);
    }

    private List<String> generateUploadUrls(S3PresignedURLRequestDTO dto) {
        if (dto.getDirName() == AwsS3Directory.MEMBER_PROFILE) {
            return generateMemberResizePresignUrls(dto);
        } else {
            String contentType = dto.getContentTypeList().get(0);
            String fileName = createUserFileName(dto.getDirName().toString(), dto.getUserId(), 0, contentType);
            return List.of(getPresignedUrl(fileName));
        }
    }

    private List<String> generateMemberResizePresignUrls(S3PresignedURLRequestDTO dto) {
        List<String> urlList = new ArrayList<>();

        for (int i = 0; i < dto.getContentTypeList().size(); i++) {
            String contentType = dto.getContentTypeList().get(i);
            if (contentType.isBlank()) {
                urlList.add(null);
            } else {
                String fileName = createMemberProfileFileName(dto.getDirName().toString(), dto.getMemberProfileId(), 0, contentType);
                urlList.add(getPresignedUrl(fileName));
            }
        }

        return urlList;
    }

    private String createMemberProfileFileName(String dirName, Long memberProfileId, int index, String contentType) {
        final LocalDate date = LocalDate.now();
        return dirName + "/" + date + "-" + "memberProfileId:" + memberProfileId + "-" + index + "." + contentType;
    }

    private String createUserFileName(String dirName, Long id, int index, String contentType) {
        final LocalDate date = LocalDate.now();
        return dirName + "/" + date + "-" + "userId:" + id + "-" + index + "." + contentType;
    }

    public String getPresignedUrl(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName) {
        // ACL 관련 코드 제거
        return new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedUrlExpiration());
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);

        return expiration;
    }
}
