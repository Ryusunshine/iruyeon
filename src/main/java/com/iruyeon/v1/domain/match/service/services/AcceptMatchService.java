package com.iruyeon.v1.domain.match.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.enums.MatchStatus;
import com.iruyeon.v1.domain.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AcceptMatchService {

    private final MatchRepository matchRepository;

    /**
     * 매칭을 수락합니다.
     */
    @Transactional
    public void acceptMatch(Long matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(
                () -> new BaseException(ErrorCode.MATCH_NOT_FOUND));

        updateMatch( match);
    }

    private void updateMatch(Match match) {
        final LocalDateTime now = LocalDateTime.now();
        try {
            match.updateOpenAt(now);
            match.updateMatchStatus(MatchStatus.MATCHED);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
