package com.iruyeon.v1.domain.s3.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetImageByMemberProfileService {

    private final ClientRepository clientRepository;
    private final GetS3PresignedUrlService getS3PresignedUrlService;
    private final GetImageByFileNameService getImageByFileNameService;

    @Transactional(readOnly = true)
    public List<String> getProfileImageByMemberId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_PROFILE_NOT_FOUND));

        List<String> fileNames = client.getImages();
        if (fileNames == null || fileNames.isEmpty()) {
            return null;
        }

        try (S3Presigner presigner = getS3PresignedUrlService.createPresigner()) {
            return fileNames.stream()
                    .map(fileName -> fileName.contains("http") ? fileName
                            : getImageByFileNameService.getS3DownloadURL(presigner, fileName))
                    .toList();
        }
    }
}
