package org.jwebppy.platform.mgmt.user.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.format.annotation.DateTimeFormat;

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
	private String fgNoUsePassword;
	private String fgAccountLocked;
	private String fgPasswordLocked;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime fromValid;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime toValid;
	private CredentialsPolicyDto credentialsPolicy;

	public String getDisplayFromValid()
	{
		return CmDateFormatUtils.format(fromValid);
	}

	public String getDisplayToValid()
	{
		return CmDateFormatUtils.format(toValid);
	}

	public String getFgNoUsePassword()
	{
		return CmStringUtils.defaultString(fgNoUsePassword, PlatformCommonVo.NO);
	}

	public String getFgAccountLocked()
	{
		return CmStringUtils.defaultString(fgAccountLocked, PlatformCommonVo.NO);
	}

	public String getFgPasswordLocked()
	{
		return CmStringUtils.defaultString(fgPasswordLocked, PlatformCommonVo.NO);
	}

	public boolean isPasswordLocked()
	{
		if (CmStringUtils.equals(getFgAccountLocked(), PlatformCommonVo.YES))
		{
			return true;
		}

		return false;
	}

	public boolean isAccountLocked()
	{
		if (CmStringUtils.equals(getFgAccountLocked(), PlatformCommonVo.YES))
		{
			return true;
		}

		return false;
	}

	public boolean isValidPeriod()
	{
        LocalDateTime localDateTime = LocalDateTime.now();

        if (fromValid != null && toValid != null)
        {
            if (localDateTime.isAfter(fromValid) && localDateTime.isBefore(toValid))
            {
            	return true;
            }
        }
        else if (fromValid != null && toValid == null)
        {
        	if (localDateTime.isAfter(fromValid))
        	{
        		return true;
        	}
        }
        else if (fromValid == null && toValid != null)
        {
        	if (localDateTime.isBefore(toValid))
        	{
        		return true;
        	}
        }

        return false;
	}
}
