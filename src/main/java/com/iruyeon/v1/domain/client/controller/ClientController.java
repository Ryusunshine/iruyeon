package com.iruyeon.v1.domain.client.controller;

import com.iruyeon.v1.config.response.Response;
import com.iruyeon.v1.domain.client.dto.ClientRequestDTO;
import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.dto.SearchRequestDTO;
import com.iruyeon.v1.domain.client.service.ClientFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientFacade clientFacade;

    @PostMapping(value = "upload")
    public ResponseEntity<Response<ClientResponseDTO>> uploadMemberProfile(
            @RequestPart("clientRequestDTO") ClientRequestDTO clientRequestDTO,
            @RequestPart("fileList") List<MultipartFile> fileList
    ) {
        return ResponseEntity.ok(
                Response.of(
                        clientFacade.saveClient(fileList, clientRequestDTO),
                        "success"
                ));
    }

    @GetMapping
    public ResponseEntity<Response<ClientResponseDTO>> getMemberProfile(
            @RequestParam("id") Long id
    ) {
        return ResponseEntity.ok(
                Response.of(
                        clientFacade.getClient(id),
                        "success"
                ));
    }

    @PutMapping
    public ResponseEntity<Response<ClientResponseDTO>> updateMemberProfile(
            @RequestPart("clientRequestDTO") ClientRequestDTO clientRequestDTO,
            @RequestPart("fileList") List<MultipartFile> fileList
    ) {
        clientFacade.updateClient(fileList, clientRequestDTO);
        return ResponseEntity.ok(
                Response.of(
                        null,
                        "success"
                ));
    }

    @DeleteMapping
    public ResponseEntity<Response<Void>> deleteMemberProfile(
            @RequestParam("id") Long id)
    {
        clientFacade.deleteClient(id);
        return ResponseEntity.ok(
                Response.of(
                        null,
                        "success"
                ));
    }

    @PostMapping("/search")
    public ResponseEntity<Response<List<ClientResponseDTO>>> searchClient(
            @RequestBody SearchRequestDTO dto
    ) {
        return ResponseEntity.ok(
                Response.of(
                        clientFacade.searchClient(dto),
                        "success"
                ));
    }
}
