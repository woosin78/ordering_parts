package org.jwebppy.platform.core.security.authentication;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AccountLockedReason extends GeneralDto
{
	private static final long serialVersionUID = -365411102380828962L;

	private LocalDateTime loginFreezingBy;
	private CredentialsPolicyDto credentialsPolicy;
}
