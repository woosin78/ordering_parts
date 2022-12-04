package org.jwebppy.platform.mgmt.user.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
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
public class UserAccountDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 3024758174943861755L;

	private Integer useq;
	private String username;
	private String password;
	@Builder.Default
	private String fgNoUsePassword = MgmtCommonVo.NO;
	@Builder.Default
	private String fgAccountLocked = MgmtCommonVo.NO;
	@Builder.Default
	private String fgPasswordLocked = MgmtCommonVo.NO;
	private LocalDateTime fromValid;
	private LocalDateTime toValid;
	private CredentialsPolicyDto credentialsPolicy;
	private UserPasswordChangeHistoryDto userPasswordChangeHistory;

	public String getDisplayFromValid()
	{
		return CmDateFormatUtils.format(fromValid);
	}

	public String getDisplayToValid()
	{
		return CmDateFormatUtils.format(toValid);
	}

	public boolean isPasswordLocked()
	{
		if (CmStringUtils.equals(getFgAccountLocked(), MgmtCommonVo.YES))
		{
			return true;
		}

		return false;
	}

	public boolean isAccountLocked()
	{
		if (CmStringUtils.equals(getFgAccountLocked(), MgmtCommonVo.YES))
		{
			return true;
		}

		return false;
	}

	public boolean isValidPeriod()
	{
		ZonedDateTime now = CmDateTimeUtils.now();
		ZonedDateTime localFromValid = CmDateTimeUtils.toZonedDateTime(fromValid);
		ZonedDateTime localToValid = CmDateTimeUtils.toZonedDateTime(toValid);

		if (ObjectUtils.isNotEmpty(localFromValid) && ObjectUtils.isNotEmpty(localToValid))
		{
            if (now.isAfter(localFromValid) && now.isBefore(localToValid))
            {
            	return true;
            }
        }
        else if (ObjectUtils.isNotEmpty(localFromValid) && ObjectUtils.isEmpty(localToValid))
        {
        	if (now.isAfter(localFromValid))
        	{
        		return true;
        	}
        }
        else if (ObjectUtils.isEmpty(localFromValid) && ObjectUtils.isNotEmpty(localToValid))
        {
        	if (now.isBefore(localToValid))
        	{
        		return true;
        	}
        }

        return false;
	}

	public boolean isValid()
	{
		return !isPasswordLocked() && !isAccountLocked() && isValidPeriod();
	}

	public boolean isEmpty()
	{
		return (useq == null);
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}
