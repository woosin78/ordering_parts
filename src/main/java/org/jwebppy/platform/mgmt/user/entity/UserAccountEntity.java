package org.jwebppy.platform.mgmt.user.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(exclude = "password")
@SuperBuilder
@NoArgsConstructor
public class UserAccountEntity extends MgmtGeneralEntity
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
	private UserPasswordChangeHistoryEntity userPasswordChangeHistory;
}
