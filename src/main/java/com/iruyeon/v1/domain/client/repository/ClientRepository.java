package com.iruyeon.v1.domain.client.repository;

import com.iruyeon.v1.domain.client.entity.Client;
import com.iruyeon.v1.domain.client.enums.MaritalStatus;
import com.iruyeon.v1.domain.member.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c " +
            "FROM Client c " +
            "WHERE c.status = com.iruyeon.v1.domain.common.enums.Status.ACTIVE " +
            "AND (:gender IS NULL OR c.gender IN :gender) " +
            "AND (:universities IS NULL OR c.university IN :universities) " +
            "AND (:eduDegree IS NULL OR c.eduDegree IN :eduDegree) " +
            "AND (:job IS NULL OR c.currentJob IN :job) " +
            "AND (c.birthYear BETWEEN :minBirthYear AND :maxBirthYear) " +
            "AND (:maritalStatus IS NULL OR c.maritalStatus IN :maritalStatus)"
    )
    Page<Client> searchAllByQueryParams(
            @Param("universities") List<String> universities,
            @Param("eduDegree") List<String> eduDegree,
            @Param("job") List<String> job,
            @Param("minBirthYear") int minBirthYear,  // 올바른 나이 필터링을 위해 변수명 변경
            @Param("maxBirthYear") int maxBirthYear,  // 올바른 나이 필터링을 위해 변수명 변경
            @Param("gender") List<Gender> gender,  // List<Gender>로 수정
            @Param("maritalStatus") List<MaritalStatus> maritalStatus,  // 변수명 수정
            Pageable pageable
    );

}
