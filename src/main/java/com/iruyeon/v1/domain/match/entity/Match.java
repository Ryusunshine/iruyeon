package com.iruyeon.v1.domain.match.entity;

import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.common.BaseEntity;
import com.iruyeon.v1.domain.match.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_match")
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_client_id")
    private Client toClient; // 받은 회원 프로필 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_client_id")
    private Client fromClient; // 보낸 회원 프로필 아이디

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    private String message;

    private LocalDateTime openAt;

    @Builder
    public Match(Long id, String message, Client toClient, Client fromClient) {
        this.matchStatus = MatchStatus.PENDING;;
        this.message = message;
        this.openAt = LocalDateTime.now();
        this.fromClient = fromClient;
        this.toClient = toClient;
    }

    public void updateMatchStatus(MatchStatus newMatchStatus) {
        this.matchStatus = newMatchStatus;
    }

    public void updateOpenAt(LocalDateTime newOpenAt) {
        this.openAt = newOpenAt;
    }

    public void updateMessage(String message) {
        this.message = message;
    }
}
