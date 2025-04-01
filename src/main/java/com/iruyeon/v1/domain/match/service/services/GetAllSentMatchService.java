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

@Service
@RequiredArgsConstructor
public class GetAllSentMatchService {

    private final MatchRepository matchRepository;
    private final GetImageByMemberProfileService getImageByMemberProfileService;

    /**
     * 내가 보낸 모든 프로필
     */

    @Transactional(readOnly = true)
    public Page<MatchThumbnailResponseDTO> getAllSentMatchService(Member member) {
        Pageable pageable = PageRequest.of(0, 6);  // 페이지 크기 6, 첫 번째 페이지
        Page<Match> matchPage = matchRepository.findAllByFromMemberAndMatchStatus(member, pageable);  // 페이징된 Match 객체들

        return matchPage.map(match -> {
            List<String> profileImages = getImageByMemberProfileService.getProfileImageByMemberId(match.getFromClient().getId());
            return MatchThumbnailResponseDTO.sentMatch(match, profileImages);
        });
    }
}
