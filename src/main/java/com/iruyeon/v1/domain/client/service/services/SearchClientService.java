package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.dto.SearchRequestDTO;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.enums.MaritalStatus;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.s3.service.services.GetImageByMemberProfileService;
import com.iruyeon.v1.domain.member.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchClientService {

    private final ClientRepository clientRepository;
    private final GetImageByMemberProfileService getImageByMemberProfileService;

    //key -> value
    // tag -> id(1, 2, 3, 4, 5) set
    // 검색 결과에 맞는 id 값을 저장해서 검색 성능을 증가시킨다
//    @Cacheable(value = "clients", key = "#dto.toCacheKey()")
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> searchClient(SearchRequestDTO dto) {
        Pageable pageable = PageRequest.of(0, 6);

        List<String> job = new ArrayList<>(dto.getJob());
        List<String> eduDegree = new ArrayList<>(dto.getEduDegree());
        List<String> university = new ArrayList<>(dto.getUniversities());
        List<Gender> genders = dto.getGender().stream().map(Gender::valueOf).toList();
        List<MaritalStatus> maritalStatuses = dto.getMaritalStatus().stream().map(MaritalStatus::valueOf).toList();

        Page<Client> clients = clientRepository.searchAllByQueryParams(university, eduDegree, job, dto.getMinAge(), dto.getMaxAge(), genders, maritalStatuses, pageable);

        return clients.stream()
                .map(memberProfile ->
                        ClientResponseDTO.of(
                                memberProfile,
                                getImageByMemberProfileService.getProfileImageByMemberId(memberProfile.getId())
                        )
                ).toList();
    }
}
