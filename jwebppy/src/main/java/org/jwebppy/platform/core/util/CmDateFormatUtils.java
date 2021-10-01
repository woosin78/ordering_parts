package org.jwebppy.platform.core.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;

public class CmDateFormatUtils
{
	public static String format(LocalDateTime localDateTime)
	{
		if (localDateTime == null)
		{
			return null;
		}

		return format(ZonedDateTime.of(localDateTime, CmDateTimeUtils.getZoneId(null)));
	}

	public static String format(LocalDateTime localDateTime, String zoneId)
	{
		return format(ZonedDateTime.of(localDateTime, CmDateTimeUtils.getZoneId(zoneId)));
	}

	public static String format(LocalDateTime localDateTime, String zoneId, String format)
	{
		if (localDateTime == null)
		{
			return null;
		}

		return format(ZonedDateTime.of(localDateTime, CmDateTimeUtils.getZoneId(zoneId)), format);
	}

	public static String format(ZonedDateTime zonedDateTime)
	{
		return format(zonedDateTime, null);
	}

	public static String format(ZonedDateTime zonedDateTime, String format)
	{
		if (zonedDateTime == null)
		{
			return null;
		}

		if (CmStringUtils.isEmpty(format))
		{
			return zonedDateTime.format(DateTimeFormatter.ofPattern(getDateTimeFormat()));
		}
		else
		{
			return zonedDateTime.format(DateTimeFormatter.ofPattern(format));
		}
	}

	public static String now()
	{
		return format(CmDateTimeUtils.now());
	}

	public static String now(String zoneId)
	{
		return format(ZonedDateTime.now(CmDateTimeUtils.getZoneId(zoneId)));
	}

	public static String now(String zoneId, String format)
	{
		return format(ZonedDateTime.now(CmDateTimeUtils.getZoneId(zoneId)), format);
	}

	public static String today()
	{
		return format(CmDateTimeUtils.now(getTimezone()), getDateFormat());
	}

	public static String today(String zoneId)
	{
		return format(CmDateTimeUtils.now(zoneId), getDateFormat());
	}

	public static String plusDays(String zoneId, long days)
	{
		return format(CmDateTimeUtils.now(zoneId).plusDays(days), null);
	}

	public static String plusDays(String zoneId, int days, String format)
	{
		return format(CmDateTimeUtils.now(zoneId).plusDays(days), format);
	}

	public static String plusWeeks(String zoneId, long weeks)
	{
		return format(CmDateTimeUtils.now(zoneId).plusWeeks(weeks), null);
	}

	public static String plusWeeks(String zoneId, long weeks, String format)
	{
		return format(CmDateTimeUtils.now(zoneId).plusWeeks(weeks), format);
	}

	public static String plusMonths(String zoneId, long months)
	{
		return format(CmDateTimeUtils.now(zoneId).plusMonths(months), null);
	}

	public static String plusMonths(String zoneId, long months, String format)
	{
		return format(CmDateTimeUtils.now(zoneId).plusMonths(months), format);
	}

	public static String plusYears(String zoneId, long years)
	{
		return format(CmDateTimeUtils.now(zoneId).plusYears(years), null);
	}

	public static String plusYears(String zoneId, long years, String format)
	{
		return format(CmDateTimeUtils.now(zoneId).plusMonths(years), format);
	}

	public static String theFirstDateMonth(ZonedDateTime zonedDateTime)
	{
		return format(zonedDateTime.withDayOfMonth(1), getDateFormat());
	}

	public static String theFirstDateThisMonth()
	{
		return theFirstDateMonth(CmDateTimeUtils.now());
	}

	public static String unlimitDate(String format)
	{
		return format(LocalDateTime.parse(PlatformCommonVo.UNLIMITED_DATE_TIME, DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)), null, format);
	}

	public static String unlimitDate()
	{
		return unlimitDate(getDateTimeFormat());
	}

	public static String getDateTimeFormat()
	{
		PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

		if (platformUserDetails == null)
		{
			return PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT;
		}

		return platformUserDetails.getDateTimeFormat1();
	}

	public static String getDateFormat()
	{
		PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

		if (platformUserDetails == null)
		{
			return PlatformCommonVo.DEFAULT_DATE_FORMAT;
		}

		return platformUserDetails.getDateFormat1();
	}

	public static String getTimeFormat()
	{
		PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

		if (platformUserDetails == null)
		{
			return PlatformCommonVo.DEFAULT_TIME_FORMAT;
		}

		return platformUserDetails.getDateFormat1();
	}

	public static String getTimezone()
	{
		PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

		if (platformUserDetails == null)
		{
			return PlatformCommonVo.DEFAULT_TIMEZONE;
		}

		return platformUserDetails.getTimezone();
	}
}
