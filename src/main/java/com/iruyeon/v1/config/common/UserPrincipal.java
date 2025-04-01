package com.iruyeon.v1.config.common;

import com.iruyeon.v1.domain.member.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserPrincipal extends UserDetails {
    String getEmail();
    String getName();
    Role getRole();
}
