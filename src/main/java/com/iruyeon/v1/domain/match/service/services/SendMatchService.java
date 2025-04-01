package com.iruyeon.v1.domain.match.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.repository.ClientRepository;
import com.iruyeon.v1.domain.common.enums.Status;
import com.iruyeon.v1.domain.match.dto.MatchRequestDTO;
import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.repository.MatchRepository;

import com.iruyeon.v1.domain.member.entity.Member;
import com.iruyeon.v1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendMatchService {

    private final MatchRepository matchRepository;
    private final ClientRepository clientRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void sendMatch(MatchRequestDTO matchRequestDTO) {
        Client fromClient = getClient(matchRequestDTO.getFromClientId());
        Client toClient = getClient(matchRequestDTO.getToClientId());

        validateClientStatus(toClient);

        Member toMember = getMember(matchRequestDTO.getToMemberId());

        validateMemberStatus(toMember);

        createMatch(fromClient, toClient, matchRequestDTO.getMessage());
    }

    private Client getClient(Long profileId) {
        return clientRepository.findById(profileId)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_PROFILE_NOT_FOUND));
    }

    private Member getMember(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateClientStatus(Client client) {
        if (client.getStatus() != Status.ACTIVE) {
            throw new BaseException(ErrorCode.MEMBER_NOT_ACTIVE);
        }
    }

    private void validateMemberStatus(Member member) {
        if (member.getStatus() != Status.ACTIVE) {
            throw new BaseException(ErrorCode.USER_NOT_ACTIVE);
        }
    }

    private void createMatch(Client fromClient, Client toClient, String message) {
        matchRepository.save(Match.builder()
                .fromClient(fromClient)
                .toClient(toClient)
                .message(message)
                .build());
    }
}
