package com.iruyeon.v1.domain.match.repository;

import com.iruyeon.v1.domain.match.entity.Match;
import com.iruyeon.v1.domain.match.enums.MatchStatus;
import com.iruyeon.v1.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m " +
            "WHERE m.fromClient.member = :member " +
            "AND m.matchStatus <> com.iruyeon.v1.domain.match.enums.MatchStatus.DELETED ")
    Page<Match> findAllByFromMemberAndMatchStatus(@Param("member") Member member, Pageable pageable);

    @Query("SELECT m FROM Match m " +
            "WHERE m.toClient.member = :member " +
            "AND m.matchStatus <> com.iruyeon.v1.domain.match.enums.MatchStatus.DELETED ")
    Page<Match> findAllByToMemberAndMatchStatus(@Param("member") Member member, Pageable pageable);

    @Query("SELECT m FROM Match m " +
            "WHERE (m.fromClient.member = :member " +
            "OR m.toClient.member = :member ) " +
            "AND m.matchStatus = :matchStatus")
    Page<Match> findAllByMatchStatus(
            @Param("member") Member member,
            @Param("matchStatus") MatchStatus matchStatus,
            Pageable pageable);
}
