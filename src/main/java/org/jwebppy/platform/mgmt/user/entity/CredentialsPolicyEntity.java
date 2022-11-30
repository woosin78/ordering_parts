package org.jwebppy.platform.mgmt.user.entity;

import java.util.List;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CredentialsPolicyEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = -5712552286897710540L;

	private Integer cpSeq;
	private String name;
	private String description;
	private String uMinLength;
	private String uMaxLength;
	private String uMinUppercase;
	private String uMaxUppercase;
	private String uMinLowercase;
	private String uMaxLowercase;
	private String uFgOnlyUppercase;
	private String uFgOnlyLowercase;
	private String uMinNumber;
	private String uMaxNumber;
	private String uMinSpecial;
	private String uMaxSpecial;
	private String pMinLength;
	private String pMaxLength;
	private String pMinUppercase;
	private String pMaxUppercase;
	private String pMinLowercase;
	private String pMaxLowercase;
	private String pMinNumber;
	private String pMaxNumber;
	private String pMinSpecial;
	private String pMaxSpecial;
	private String pValidPeriod;
	private String fgUsePwdFailPenalty;
	private String pFailCheckDuration;
	private String pAllowableFailCount;
	private String pFreezingDuration;
	private String fgUse;
	private String fgDefault;
	private List<UserGroupEntity> userGroups;
}
