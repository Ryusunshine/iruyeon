package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.s3.service.services.GetImageByFileNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetClientService {

    private final ClientRepository clientRepository;
    private final GetImageByFileNameService getImageByFileNameService;

    @Transactional(readOnly = true)
    public ClientResponseDTO getClient(Long memberId) {
        Client client = clientRepository.findById(memberId).orElseThrow(
                () -> new BaseException(ErrorCode.MEMBER_PROFILE_NOT_FOUND));
        List<String> profileImages = client.getImages().stream().map(getImageByFileNameService::getImageByFileName).toList();
        return ClientResponseDTO.of(client, profileImages);
    }
}
