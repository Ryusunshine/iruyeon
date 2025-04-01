package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.dto.ClientRequestDTO;
import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.dto.FamilyRequestDTO;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.entity.Family;
import com.iruyeon.v1.domain.client.enums.MaritalStatus;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.member.enums.Gender;
import com.iruyeon.v1.domain.s3.service.services.GetImageByFileNameService;
import com.iruyeon.v1.domain.s3.service.services.UploadImageService;
import com.iruyeon.v1.domain.search.entity.ClientDocument;
import com.iruyeon.v1.domain.search.service.ClientSearchFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveClientService {

    private final ClientRepository clientRepository;
    private final UploadImageService uploadImageService;
    private final GetImageByFileNameService getImageByFileNameService;
    private final CreateFamilyService createFamilyService;
    private final ClientSearchFacade clientSearchFacade;

    @Transactional
    public ClientResponseDTO saveClient(List<MultipartFile> fileList, ClientRequestDTO dto) {
        // 가족 생성
        Set<Family> family = createFamily(dto);

        Client client = createClient(dto, family);

        // 이미지 업로드
        List<String> filePaths = fileList.stream().map(uploadImageService::uploadImage).toList();
        updateClientImageList(client, filePaths);

        // elasticSearch에 저장
        clientSearchFacade.saveClient(ClientDocument.entityToDocument(client));

        return ClientResponseDTO.of(client,
                client.getImages().stream()
                        .map(getImageByFileNameService::getImageByFileName)
                        .toList()
        );
    }

    private Client createClient(ClientRequestDTO memberRequestDTO, Set<Family> family) {
        return clientRepository.save(Client.builder()
                .name(memberRequestDTO.getName())
                .phoneNumber(memberRequestDTO.getPhoneNumber())
                .address(memberRequestDTO.getAddress())
                .birthYear(memberRequestDTO.getBirthYear())
                .highSchool(memberRequestDTO.getHighSchool())
                .university(memberRequestDTO.getUniversity())
                .currentJob(memberRequestDTO.getCurrentJob())
                .previousJob(memberRequestDTO.getPreviousJob())
                .jobDetail(memberRequestDTO.getJobDetail())
                .info(memberRequestDTO.getInfo())
                .homeTown(memberRequestDTO.getHomeTown())
                .gender(Gender.valueOf(memberRequestDTO.getGender()))
                .ageGapLower(memberRequestDTO.getBirthYear() - memberRequestDTO.getAgeGapLower())
                .ageGapHigher(memberRequestDTO.getBirthYear() + memberRequestDTO.getAgeGapHigher())
                .interest(memberRequestDTO.getInterest())
                .idealType(memberRequestDTO.getIdealType())
                .personality(memberRequestDTO.getPersonality())
                .hasChild(memberRequestDTO.getHasChild())
                .maritalStatus(MaritalStatus.valueOf(memberRequestDTO.getMaritalStatus()))
                .property(memberRequestDTO.getProperty())
                .major(memberRequestDTO.getMajor())
                .family(family)
                .build());
    }

    private void updateClientImageList(Client client, List<String> imageList) {
        try {
            client.updateImage(imageList);
        } catch (Exception e) {
            log.error("updateClientImageList = {}", e.getMessage());
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }

    private Set<Family> createFamily(ClientRequestDTO dto) {
        List<FamilyRequestDTO> familyRequestDTOList = Optional.ofNullable(dto.getFamilyRequestDTOList())
                .orElseGet(Collections::emptyList);

        return familyRequestDTOList.stream()
                .map(createFamilyService::createFamily)
                .collect(Collectors.toSet());
    }
}
