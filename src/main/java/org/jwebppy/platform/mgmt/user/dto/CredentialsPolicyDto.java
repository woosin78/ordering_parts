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
	private String uMinLength;
	private String uMaxLength;
	private String uMinUppercase;
	private String uMaxUppercase;
	private String uMinLowercase;
	private String uMaxLowercase;
	@Builder.Default
	private String uFgOnlyUppercase = MgmtCommonVo.NO;
	@Builder.Default
	private String uFgOnlyLowercase = MgmtCommonVo.NO;
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
	@Builder.Default
	private String fgUsePwdFailPenalty = MgmtCommonVo.NO;
	private String pFailCheckDuration;
	private String pAllowableFailCount;
	private String pFreezingDuration;
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
