package com.iruyeon.v1.domain.search.repository;

import com.iruyeon.v1.domain.search.entity.ClientDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientDocumentRepository extends ElasticsearchRepository<ClientDocument, String> {
}
