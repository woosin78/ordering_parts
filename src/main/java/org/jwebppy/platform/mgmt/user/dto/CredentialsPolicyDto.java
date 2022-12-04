package org.jwebppy.platform.mgmt.user.dto;

import java.util.List;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CredentialsPolicyDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = 2897067694360373935L;

	private Integer cpSeq;
	private String name;
	private String description;
	private String uminLength;
	private String umaxLength;
	private String uminUppercase;
	private String umaxUppercase;
	private String uminLowercase;
	private String umaxLowercase;
	@Builder.Default
	private String ufgOnlyUppercase = MgmtCommonVo.NO;
	@Builder.Default
	private String ufgOnlyLowercase = MgmtCommonVo.NO;
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
	@Builder.Default
	private String fgUsePwdFailPenalty = MgmtCommonVo.NO;
	private String pfailCheckDuration;
	private String pallowableFailCount;
	private String pfreezingDuration;
	@Builder.Default
	private String fgUse = MgmtCommonVo.NO;
	@Builder.Default
	private String fgDefault = MgmtCommonVo.NO;
	private List<UserGroupDto> userGroups;

	public CredentialsPolicyDto(Integer cpSeq)
	{
		this.cpSeq = cpSeq;
	}
}
