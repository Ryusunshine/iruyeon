package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public void deleteClient(Long clientId) {
        try {
            clientRepository.deleteById(clientId);

        } catch (Exception e) {
            log.error("DeleteMemberProfileService - {}" , e.getMessage());
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
