package org.jwebppy.platform.mgmt.user.dto;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
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
	private List<Integer> uSeqs;
	private UserAccountDto userAccount;
	private UserContactInfoDto userContactInfo;
	private UserGroupDto userGroup;

	public UserAccountDto getUserAccount()
	{
		return ObjectUtils.defaultIfNull(userAccount, new UserAccountDto());
	}

	public UserContactInfoDto getUserContactInfo()
	{
		return ObjectUtils.defaultIfNull(userContactInfo, new UserContactInfoDto());
	}

	public UserGroupDto getUserGroup()
	{
		return ObjectUtils.defaultIfNull(userGroup, new UserGroupDto());
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

	public String getTimezone()
	{
		return CmStringUtils.defaultString(getUserContactInfo().getTimezone(), getUserGroup().getTimezone());
	}

	public boolean isEmpty()
	{
		return (uSeq == null);
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}
