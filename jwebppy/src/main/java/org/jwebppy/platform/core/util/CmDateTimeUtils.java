package org.jwebppy.platform.core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;

public class CmDateTimeUtils
{
	public static ZonedDateTime now()
	{
		return now(null);
	}

	public static ZonedDateTime now(String zoneId)
	{
		if (CmStringUtils.isEmpty(zoneId))
		{
			PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

			zoneId = (platformUserDetails != null) ? platformUserDetails.getTimezone() : PlatformCommonVo.DEFAULT_TIMEZONE;
		}

		return ZonedDateTime.now(getZoneId(zoneId));
	}

	public static LocalDateTime toLocalDateTime(String dateTime)
	{
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateTimeFormat()));
	}

	public static ZonedDateTime toZonedDateTime(Object dateTime)
	{
		if (dateTime != null)
		{
			if (dateTime instanceof LocalDateTime)
			{
				return ZonedDateTime.of((LocalDateTime)dateTime, getZoneId());
			}
			else
			{
				return ZonedDateTime.parse((String)dateTime, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateTimeFormat()));
			}
		}

		return null;
	}

	public static ZoneId getZoneId()
	{
		return getZoneId(null);
	}

	public static ZoneId getZoneId(String zoneId)
	{
		if (CmStringUtils.isEmpty(zoneId))
		{
			zoneId = CmStringUtils.defaultIfEmpty(CmDateFormatUtils.getTimezone(), PlatformCommonVo.DEFAULT_TIMEZONE);
		}

		try
		{
			return ZoneId.of(zoneId);
		}
		catch (ZoneRulesException e)
		{
			return ZoneId.of(PlatformCommonVo.DEFAULT_TIMEZONE);
		}
	}
}
