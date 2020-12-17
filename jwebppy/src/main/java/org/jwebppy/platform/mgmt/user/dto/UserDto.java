package org.jwebppy.platform.mgmt.user.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto extends GeneralDto implements IPagination, Serializable
{
	private static final long serialVersionUID = 7432904918300441795L;

	private Integer uSeq;
	private String firstName;
	private String lastName;
	private String enFirstName;
	private String enLastName;
	private String company;
	private String organization;
	private String department;
	private String position;
	private String language;
	private String fgDelete = PlatformCommonVo.NO;
	private List<Integer> uSeqs;
	private UserAccountDto userAccount;
	private UserContactInfoDto userContactInfo;

	public UserAccountDto getUserAccount()
	{
		if (userAccount == null)
		{
			return new UserAccountDto();
		}

		return userAccount;
	}

	public UserContactInfoDto getUserContactInfo()
	{
		if (userContactInfo == null)
		{
			return new UserContactInfoDto();
		}

		return userContactInfo;
	}

	public String getName()
	{
		StringBuffer name = new StringBuffer();

		if (CmStringUtils.isNotEmpty(firstName))
		{
			name.append(firstName);
		}

		if (CmStringUtils.isNotEmpty(lastName))
		{
			name.append(" ").append(lastName);
		}

		return name.toString();
	}

	public String getDisplayLanguage()
	{
		if (CmStringUtils.isNotEmpty(language))
		{
			return new Locale(language).getDisplayLanguage();
		}

		return language;
	}
}
