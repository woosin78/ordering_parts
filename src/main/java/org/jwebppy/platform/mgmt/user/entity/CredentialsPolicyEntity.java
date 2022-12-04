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
	private String uminLength;
	private String umaxLength;
	private String uminUppercase;
	private String umaxUppercase;
	private String uminLowercase;
	private String umaxLowercase;
	private String ufgOnlyUppercase;
	private String ufgOnlyLowercase;
	private String uminNumber;
	private String umaxNumber;
	private String uminSpecial;
	private String umaxSpecial;
	private String pminLength;
	private String pmaxLength;
	private String pminUppercase;
	private String pmaxUppercase;
	private String pminLowercase;
	private String pmaxLowercase;
	private String pminNumber;
	private String pmaxNumber;
	private String pminSpecial;
	private String pmaxSpecial;
	private String pvalidPeriod;
	private String fgUsePwdFailPenalty;
	private String pfailCheckDuration;
	private String pallowableFailCount;
	private String pfreezingDuration;
	private String fgUse;
	private String fgDefault;
	private List<UserGroupEntity> userGroups;
}
