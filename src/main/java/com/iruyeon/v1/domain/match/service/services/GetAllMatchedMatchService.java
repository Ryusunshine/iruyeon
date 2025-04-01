package com.iruyeon.v1.domain.match.service.services;

import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.match.dto.MatchThumbnailResponseDTO;
import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.enums.MatchStatus;
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
public class GetAllMatchedMatchService {

    private final MatchRepository matchRepository;
    private final GetImageByMemberProfileService getImageByMemberProfileService;

    @Transactional(readOnly = true)
    public Page<MatchThumbnailResponseDTO> getAllMatchedMatchService(Member member) {
        Pageable pageable = PageRequest.of(0, 6);
        Page<Match> matchPage = matchRepository.findAllByMatchStatus(member, MatchStatus.MATCHED, pageable);

        return matchPage.map(match -> {
            //매칭된 프로필에서 내가 받은 프로필이었으면
            boolean isUserToUser = member.equals(match.getToClient().getMember());
            Member oppositeMember = member.equals(match.getToClient().getMember()) ? match.getFromClient().getMember(): match.getToClient().getMember();
            Client oppositeClient = isUserToUser ? match.getFromClient() : match.getToClient();

            List<String> oppositeProfileImages = getImageByMemberProfileService.getProfileImageByMemberId(oppositeClient.getId());

            return MatchThumbnailResponseDTO.matchedMatch(oppositeMember, match, oppositeClient, oppositeProfileImages);
        });
    }
}
