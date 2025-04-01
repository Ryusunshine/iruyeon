package com.iruyeon.v1.domain.search.controller;

import com.iruyeon.v1.config.common.annotation.PublicAccess;
import com.iruyeon.v1.config.response.Response;
import com.iruyeon.v1.domain.client.dto.ClientRequestDTO;
import com.iruyeon.v1.domain.client.dto.SearchRequestDTO;
import com.iruyeon.v1.domain.search.entity.ClientDocument;
import com.iruyeon.v1.domain.search.service.ClientSearchFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PublicAccess
@RequiredArgsConstructor
@RequestMapping("/api/client/search")
public class ClientSearchController {

    private final ClientSearchFacade clientSearchFacade;

    @PostMapping
    public ResponseEntity<Response<List<ClientDocument>>> search(
            @RequestBody SearchRequestDTO dto) {
        return ResponseEntity.ok(
                Response.of(
                        clientSearchFacade.searchClient(dto),
                        "success"
                ));
    }

}
