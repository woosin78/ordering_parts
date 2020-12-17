package org.jwebppy.platform.mgmt.user.dto;

import java.io.Serializable;
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
public class UserAccountDto extends GeneralDto implements Serializable
{
	private static final long serialVersionUID = 7248657835333051815L;

	private Integer uSeq;
	private String username;
	private String password;
	private String fgAccountLocked = PlatformCommonVo.NO;
	private String fgPasswordLocked = PlatformCommonVo.NO;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime fromValid;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime toValid;

	public String getDisplayFromValid()
	{
		return CmDateFormatUtils.format(fromValid);
	}

	public String getDisplayToValid()
	{
		return CmDateFormatUtils.format(toValid);
	}

	public String getFgAccountLocked()
	{
		return CmStringUtils.defaultString(fgAccountLocked, PlatformCommonVo.NO);
	}

	public String getFgPasswordLocked()
	{
		return CmStringUtils.defaultString(fgPasswordLocked, PlatformCommonVo.NO);
	}

	public boolean isAccountLocked()
	{
		if (PlatformCommonVo.YES.equals(getFgAccountLocked()))
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
