package com.iruyeon.v1.domain.match.dto;

import com.iruyeon.v1.domain.client.dto.ClientResponseDTO;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.enums.MatchStatus;
import com.iruyeon.v1.domain.member.dto.MemberResponseDTO;
import com.iruyeon.v1.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MatchThumbnailResponseDTO {
    private Long matchId;
    private MatchStatus matchStatus;
    private MemberResponseDTO oppositeMember;
    private String message;
    private ClientResponseDTO oppositeClient;

    public static MatchThumbnailResponseDTO matchedMatch(Member oppositeMember, Match match, Client oppositeClient, List<String> oppositeProfileImages) {
        return MatchThumbnailResponseDTO.builder()
                .matchId(match.getId())
                .matchStatus(match.getMatchStatus())
                .oppositeMember(MemberResponseDTO.of(oppositeMember))
                .oppositeClient(ClientResponseDTO.of(oppositeClient, oppositeProfileImages))
                .message(match.getMessage())
                .build();
    }

    // 받은 매칭 정보
    public static MatchThumbnailResponseDTO receivedMatch(Match match, List<String> oppositeProfileImages) {
        return MatchThumbnailResponseDTO.builder()
                .matchId(match.getId())
                .matchStatus(match.getMatchStatus())
                .oppositeMember(MemberResponseDTO.of(match.getFromClient().getMember()))
                .oppositeClient(ClientResponseDTO.of(match.getToClient(), oppositeProfileImages))
                .message(match.getMessage())
                .build();
    }

    // 보낸 매칭 정보
    public static MatchThumbnailResponseDTO sentMatch(Match match, List<String> oppositeProfileImages) {
        return MatchThumbnailResponseDTO.builder()
                .matchId(match.getId())
                .matchStatus(match.getMatchStatus())
                .oppositeMember(MemberResponseDTO.of(match.getToClient().getMember()))
                .oppositeClient(ClientResponseDTO.of(match.getFromClient(), oppositeProfileImages))
                .message(match.getMessage())
                .build();
    }
}
