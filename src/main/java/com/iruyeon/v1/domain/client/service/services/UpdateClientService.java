package com.iruyeon.v1.domain.client.service.services;


import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.dto.ClientRequestDTO;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.client.dto.FamilyRequestDTO;
import com.iruyeon.v1.domain.client.entity.Family;
import com.iruyeon.v1.domain.client.enums.Relationship;
import com.iruyeon.v1.domain.s3.service.services.GetImageByFileNameService;
import com.iruyeon.v1.domain.s3.service.services.UploadImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateClientService {

    private final ClientRepository clientRepository;
    private final UpdateClientImageService updateClientImageService;
    private final GetImageByFileNameService getImageByFileNameService;
    private final UploadImageService uploadImageService;
    private final CreateFamilyService createFamilyService;


    @Transactional
    public void updateClient(List<MultipartFile> fileList, ClientRequestDTO dto) {

        Client client = clientRepository.findById(dto.getMemberProfileId()).orElseThrow(
                () -> new BaseException(ErrorCode.MEMBER_PROFILE_NOT_FOUND));

        // 가족 업데이트
        if (!dto.getFamilyRequestDTOList().isEmpty()) {
            updateFamily(client, dto);
        }

        //멤버 프로필 업데이트
        updateMemberProfile(client, fileList, dto);

    }


    private void updateMemberProfile(Client client, List<MultipartFile> fileList, ClientRequestDTO dto) {
        try {
            // 이미지 업데이트
            updateClientImageService.updateClientImage(client.getId(), fileList);
            client.update(dto);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }

    private void updateFamily(Client client, ClientRequestDTO dto) {
        Set<Family> family = new HashSet<>();
        for (FamilyRequestDTO familyRequestDTO : dto.getFamilyRequestDTOList()) {
            family.add(createFamilyService.createFamily(familyRequestDTO));
        }

        client.updateFamily(family);
    }
}
