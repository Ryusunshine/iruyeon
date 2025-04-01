package com.iruyeon.v1.domain.match.service.services;

import com.iruyeon.v1.domain.match.dto.MatchThumbnailResponseDTO;
import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.repository.MatchRepository;
import com.iruyeon.v1.domain.s3.service.services.GetImageByMemberProfileService;
import com.iruyeon.v1.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllReceivedMatchService {

    private final MatchRepository matchRepository;
    private final GetImageByMemberProfileService getImageByMemberProfileService;

    /**
     * 받은 모든 프로필 조회
     */
    @Transactional(readOnly = true)
    public  Page<MatchThumbnailResponseDTO> getAllReceivedMatchService(Member member) {
        Pageable pageable = (Pageable) PageRequest.of(0, 6);
        Page<Match> matchPage = matchRepository.findAllByToMemberAndMatchStatus(member, pageable);  // 페이징된 Match 객체들

        return matchPage.map(match -> {
            List<String> profileImages = getImageByMemberProfileService.getProfileImageByMemberId(match.getToClient().getId());
            return MatchThumbnailResponseDTO.receivedMatch(match, profileImages);
        });
    }
}
