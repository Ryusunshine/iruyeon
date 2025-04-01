package com.iruyeon.v1.config.common;

import com.iruyeon.v1.domain.member.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * ※ CustomOAuth2User를 구현하는 이유는,
 * Resource Server에서 제공하지 않는 추가 정보들을 내 서비스에서 가지고 있기 위함입니다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private Long memberId;
    private Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the member
     * @param attributes       the attributes about the member
     * @param nameAttributeKey the key used to access the member's "name" from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role, Long memberId) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.memberId = memberId;
        this.role = role;
    }
}
