package org.jwebppy.platform.mgmt.user.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")
public class UserAccountEntity extends GeneralEntity
{
	private static final long serialVersionUID = -4600399541265520059L;
	private Integer uSeq;
	private String username;
	private String password;
	private String fgNoUsePassword;
	private String fgAccountLocked;
	private String fgPasswordLocked;
	private LocalDateTime fromValid;
	private LocalDateTime toValid;
	private CredentialsPolicyEntity credentialsPolicy;
}
