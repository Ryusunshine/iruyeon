package com.iruyeon.v1.domain.token.repository;

import com.iruyeon.v1.domain.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByEmail(String email);

    RefreshToken findByToken(String refreshToken);
}
