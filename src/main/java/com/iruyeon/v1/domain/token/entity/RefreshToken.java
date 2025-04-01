package com.iruyeon.v1.domain.token.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition="TEXT")
    private String token;

    @Column(nullable = false)
    private LocalDate expiryDate;

    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.token = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.token = newRefreshToken;
        return this;
    }
}
