package org.jwebppy.platform.mgmt.user.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserAccountDto extends GeneralDto
{
	private static final long serialVersionUID = 3024758174943861755L;

	private Integer uSeq;
	private String username;
	private String password;
	private String fgNoUsePassword = MgmtCommonVo.NO;
	private String fgAccountLocked = MgmtCommonVo.NO;
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

        if (localFromValid != null && localToValid != null)
        {
            if (now.isAfter(localFromValid) && now.isBefore(localToValid))
            {
            	return true;
            }
        }
        else if (localFromValid != null && localToValid == null)
        {
        	if (now.isAfter(localFromValid))
        	{
        		return true;
        	}
        }
        else if (localFromValid == null && localToValid != null)
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
		return (uSeq == null);
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}
