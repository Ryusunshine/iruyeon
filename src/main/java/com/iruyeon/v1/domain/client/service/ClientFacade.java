package com.iruyeon.v1.domain.client.service;

import com.iruyeon.v1.domain.client.dto.ClientRequestDTO;
import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.dto.SearchRequestDTO;
import com.iruyeon.v1.domain.client.service.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientFacade {

    private final SaveClientService saveClientService;
    private final GetClientService getClientService;
    private final DeleteClientService deleteClientService;
    private final UpdateClientService updateClientService;
    private final SearchClientService searchClientService;

    @Transactional
    public ClientResponseDTO saveClient(List<MultipartFile> fileList, ClientRequestDTO dto) {
        return saveClientService.saveClient(fileList, dto);
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO getClient(Long memberId) {
        return getClientService.getClient(memberId);
    }

    @Transactional(readOnly = true)
    public void deleteClient(Long memberId) {
        deleteClientService.deleteClient(memberId);
    }

    @Transactional
    public void updateClient(List<MultipartFile> fileList, ClientRequestDTO dto) {
        updateClientService.updateClient(fileList, dto);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> searchClient(SearchRequestDTO dto) {
        return searchClientService.searchClient(dto);
    }
}
