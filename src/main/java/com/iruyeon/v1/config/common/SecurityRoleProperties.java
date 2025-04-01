package com.iruyeon.v1.config.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.roles")
public class SecurityRoleProperties {
	private List<String> member;
	private List<String> approved;
	private List<String> memberApproved;
	private List<String> memberDisApproved;

	public String[] getMemberRoles() {
		return convertToRoles(member);
	}

	public String[] getApprovedRoles() {
		return convertToRoles(approved);
	}

	public String[] getMemberApprovedRoles() {
		return convertToRoles(memberApproved);
	}

	public String[] getMemberDisApprovedRoles() {
		return convertToRoles(memberDisApproved);
	}

	private String[] convertToRoles(List<String> roles) {
		return roles.stream()
			.map(this::convertToRole)
			.toList()
			.toArray(new String[0]);
	}

	private String convertToRole(String role) {
		String uppercaseRole = role.toUpperCase();
		if (uppercaseRole.startsWith("ROLE_")) {
			return uppercaseRole.substring(5);
		}
		return uppercaseRole;
	}
}
