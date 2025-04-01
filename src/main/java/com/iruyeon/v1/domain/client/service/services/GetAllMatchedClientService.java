package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllMatchedClientService {

    private final MatchRepository matchRepository;
    private final ClientRepository clientRepository;


}
