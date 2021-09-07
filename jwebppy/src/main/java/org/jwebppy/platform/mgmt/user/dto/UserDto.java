package org.jwebppy.platform.mgmt.user.dto;

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
public class UserDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -3480128973325366274L;

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
	private String fgDelete;
	private List<Integer> uSeqs;
	private UserAccountDto userAccount;
	private UserContactInfoDto userContactInfo;
	private UserGroupDto userGroup;

	public String getFgDelete()
	{
		return CmStringUtils.defaultIfEmpty(fgDelete, PlatformCommonVo.NO);
	}

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

	public UserGroupDto getUserGroup()
	{
		if (userGroup == null)
		{
			return new UserGroupDto();
		}

		return userGroup;
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
