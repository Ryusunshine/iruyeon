package com.iruyeon.v1.domain.search.service;

import com.iruyeon.v1.domain.client.dto.SearchRequestDTO;
import com.iruyeon.v1.domain.search.entity.ClientDocument;
import com.iruyeon.v1.domain.search.repository.ClientDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientSearchFacade {

    private final ElasticsearchOperations operations;
    private final ClientDocumentRepository clientDocumentRepository;

    public ClientDocument saveClient(ClientDocument document) {
        return clientDocumentRepository.save(document);
    }

    public List<ClientDocument> searchClient(SearchRequestDTO dto) {
        // 페이지 설정
        Pageable pageable = PageRequest.of(0, 10);

        // 검색 조건 설정
        Criteria criteria = createConditionCriteria(dto);
        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        // Elasticsearch 검색 실행
        SearchHits<ClientDocument> searchHits = operations.search(query, ClientDocument.class);

        // 검색 결과 변환 후 반환
        return searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private Criteria createConditionCriteria(SearchRequestDTO dto) {
        Criteria criteria = new Criteria();

        if (dto == null) {
            return criteria;
        }

        // 직업 조건
        if (dto.getJob() != null && !dto.getJob().isEmpty()) {
            criteria = criteria.and(Criteria.where("currentJob").in(dto.getJob()));
        }

        // 나이 범위 조건
        if (dto.getMinAge() > 0) {
            criteria = criteria.and(Criteria.where("age").greaterThanEqual(dto.getMinAge()));
        }
        if (dto.getMaxAge() > 0) {
            criteria = criteria.and(Criteria.where("age").lessThanEqual(dto.getMaxAge()));
        }

        // 학력 조건
        if (dto.getEduDegree() != null && !dto.getEduDegree().isEmpty()) {
            criteria = criteria.and(Criteria.where("eduDegree").in(dto.getEduDegree()));
        }

        // 대학교 조건
        if (dto.getUniversities() != null && !dto.getUniversities().isEmpty()) {
            criteria = criteria.and(Criteria.where("university").in(dto.getUniversities()));
        }

        // 성별 조건
        if (dto.getGender() != null && !dto.getGender().isEmpty()) {
            criteria = criteria.and(Criteria.where("gender").in(dto.getGender()));
        }

        // 결혼 상태 조건
        if (dto.getMaritalStatus() != null && !dto.getMaritalStatus().isEmpty()) {
            criteria = criteria.and(Criteria.where("maritalStatus").in(dto.getMaritalStatus()));
        }

        return criteria;
    }
}
