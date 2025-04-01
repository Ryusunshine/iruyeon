package com.iruyeon.v1.domain.match.service;

import com.iruyeon.v1.domain.match.dto.MatchRequestDTO;
import com.iruyeon.v1.domain.match.dto.MatchThumbnailResponseDTO;
import com.iruyeon.v1.domain.match.service.services.AcceptMatchService;
import com.iruyeon.v1.domain.match.service.services.RejectMatchService;
import com.iruyeon.v1.domain.match.service.services.SendMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchFacade {

    private final AcceptMatchService acceptMatchService;
    private final RejectMatchService rejectMatchService;
    private final SendMatchService sendMatchService;

    @Transactional
    public void acceptMatch(Long matchId) {
        acceptMatchService.acceptMatch(matchId);
    }

    @Transactional
    public void rejectMatch(Long matchId) {
        rejectMatchService.rejectMatch(matchId);
    }

    @Transactional
    public void sendMatch(MatchRequestDTO matchRequestDTO) {
        sendMatchService.sendMatch(matchRequestDTO);
    }
}
