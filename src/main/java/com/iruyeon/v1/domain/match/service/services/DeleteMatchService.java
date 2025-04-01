package com.iruyeon.v1.domain.match.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.enums.MatchStatus;
import com.iruyeon.v1.domain.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteMatchService {

    private final MatchRepository matchRepository;

    @Transactional
    public void deleteMatch(Long matchId) {
        try {
            Match match = matchRepository.findById(matchId).orElseThrow(()
                    -> new BaseException(ErrorCode.MATCH_NOT_FOUND));

            match.updateMatchStatus(MatchStatus.DELETED);

        } catch (Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR);
        }
    }
}
