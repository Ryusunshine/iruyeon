package com.iruyeon.v1.domain.member.repository;

import com.iruyeon.v1.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Member findBySocialId(String socialId);
}
