package org.jwebppy.platform.core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;

import org.jwebppy.platform.core.PlatformCommonVo;

public class CmDateFormatUtils
{
	public static String format(LocalDateTime localDateTime)
	{
		if (localDateTime != null)
		{
			return format(ZonedDateTime.of(localDateTime, getZoneId(null)));
		}

		return null;
	}

	public static String format(LocalDateTime localDateTime, String zoneId)
	{
		return format(ZonedDateTime.of(localDateTime, getZoneId(zoneId)));
	}

	public static String format(LocalDateTime localDateTime, String zoneId, String format)
	{
		return format(ZonedDateTime.of(localDateTime, getZoneId(zoneId)), format);
	}

	public static String format(ZonedDateTime zonedDateTime)
	{
		return format(zonedDateTime, null);
	}

	public static String format(ZonedDateTime zonedDateTime, String format)
	{
		if (zonedDateTime != null)
		{
			if (CmStringUtils.isEmpty(format))
			{
				return zonedDateTime.format(DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT));
			}
			else
			{
				return zonedDateTime.format(DateTimeFormatter.ofPattern(format));
			}
		}

		return null;
	}

	public static ZoneId getZoneId(String zoneId)
	{
		if (CmStringUtils.isEmpty(zoneId))
		{
			zoneId = PlatformCommonVo.DEFAULT_TIMEZONE;
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

	public static ZonedDateTime zonedNow()
	{
		return zonedNow(null);
	}

	public static ZonedDateTime zonedNow(String zoneId)
	{
		if (CmStringUtils.isNotEmpty(zoneId))
		{
			zoneId = PlatformCommonVo.DEFAULT_TIMEZONE;
		}

		return ZonedDateTime.now(getZoneId(zoneId));
	}

	public static String now()
	{
		return format(zonedNow());
	}

	public static String now(String zoneId)
	{
		return format(ZonedDateTime.now(getZoneId(zoneId)));
	}

	public static String now(String zoneId, String format)
	{
		return format(ZonedDateTime.now(getZoneId(zoneId)), format);
	}

	public static String today()
	{
		return format(zonedNow(PlatformCommonVo.DEFAULT_TIMEZONE), PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public static String today(String zoneId)
	{
		return format(zonedNow(zoneId), PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public static String plusDays(String zoneId, long days)
	{
		return format(zonedNow(zoneId).plusDays(days), null);
	}

	public static String plusDays(String zoneId, int days, String format)
	{
		return format(zonedNow(zoneId).plusDays(days), format);
	}

	public static String plusWeeks(String zoneId, long weeks)
	{
		return format(zonedNow(zoneId).plusWeeks(weeks), null);
	}

	public static String plusWeeks(String zoneId, long weeks, String format)
	{
		return format(zonedNow(zoneId).plusWeeks(weeks), format);
	}

	public static String plusMonths(String zoneId, long months)
	{
		return format(zonedNow(zoneId).plusMonths(months), null);
	}

	public static String plusMonths(String zoneId, long months, String format)
	{
		return format(zonedNow(zoneId).plusMonths(months), format);
	}

	public static String plusYears(String zoneId, long years)
	{
		return format(zonedNow(zoneId).plusYears(years), null);
	}

	public static String plusYears(String zoneId, long years, String format)
	{
		return format(zonedNow(zoneId).plusMonths(years), format);
	}

	public static String theFirstDateMonth(ZonedDateTime zonedDateTime)
	{
		return format(zonedDateTime.withDayOfMonth(1), PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public static String theFirstDateThisMonth()
	{
		return theFirstDateMonth(zonedNow());
	}

	public static String unlimitDate(String format)
	{
		return format(ZonedDateTime.parse(PlatformCommonVo.UNLIMITED_DATE_TIME, DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)), format);
	}

	public static String unlimitDate()
	{
		return unlimitDate(PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}
}
