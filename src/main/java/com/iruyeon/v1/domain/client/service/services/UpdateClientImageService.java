package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.s3.service.services.GetImageByFileNameService;
import com.iruyeon.v1.domain.s3.service.services.UploadImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateClientImageService {

    private final ClientRepository clientRepository;
    private final GetImageByFileNameService getImageByFileNameService;
    private final UploadImageService uploadImageService;

    @Transactional
    public ClientResponseDTO updateClientImage(Long memberProfileId, List<MultipartFile> files) {
        Client client = clientRepository.findById(memberProfileId).orElseThrow(() -> new BaseException(ErrorCode.MEMBER_PROFILE_NOT_FOUND));

        List<String> newFileList = files.stream().map(uploadImageService::uploadImage).toList();
        updateClientImage(client, newFileList);
        List<String> newImageUrl = createFullUrlListResponse(newFileList);

        return ClientResponseDTO.of(client, newImageUrl);
    }

    private List<String> mergeImages(Client client, List<MultipartFile> files) {
        List<String> originalImages = client.getImages();
        List<String> updatedImages = new ArrayList<>();

        if (originalImages == null) {
            files.stream().map(uploadImageService::uploadImage).forEach(updatedImages::add);
        } else {
            for (int i = 0; i < files.size(); i++) {
                String newImage = files.get(i).getOriginalFilename();
                String originalImage = originalImages.get(i).isBlank() ? null : originalImages.get(i);
                if (newImage != null && !newImage.equals(originalImage)) {
                    updatedImages.add(uploadImageService.uploadImage(files.get(i)));
                } else {
                    updatedImages.add(originalImage);
                }
            }
        }
        updateClientImage(client, updatedImages);
        return createFullUrlListResponse(updatedImages);
    }

    private List<String> createFullUrlListResponse(List<String> imageUrlList) {
        List<String> fullUrlList = new ArrayList<>();
        imageUrlList.forEach(image -> fullUrlList.add(getImageByFileNameService.getImageByFileName(image)));
        return fullUrlList;
    }

    private void updateClientImage(Client client, List<String> imageUrlList) {
        try {
            client.updateImage(imageUrlList);
        } catch (Exception e) {
            log.error("Failed to update member image status: {}", e.getMessage(), e);
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
