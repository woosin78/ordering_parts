package org.jwebppy.platform.mgmt.user.dto;

import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CredentialsPolicyDto extends GeneralDto implements IPagination
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
	private String uFgOnlyUppercase = PlatformCommonVo.NO;
	private String uFgOnlyLowercase = PlatformCommonVo.NO;
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
	private int pwdValidPeriod;
	private String fgUse = PlatformCommonVo.NO;
	private String fgDefault = PlatformCommonVo.NO;
	private List<UserGroupDto> userGroups;

	public CredentialsPolicyDto() {}

	public CredentialsPolicyDto(Integer cpSeq)
	{
		this.cpSeq = cpSeq;
	}
}
