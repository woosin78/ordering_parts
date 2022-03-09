package org.jwebppy.platform.core.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;

import org.jwebppy.platform.core.PlatformCommonVo;

public class CmDateTimeUtils
{
	public static ZonedDateTime now()
	{
		return now(null);
	}

	public static ZonedDateTime now(String zoneId)
	{
		return ZonedDateTime.now().withZoneSameInstant(getZoneId(zoneId));
	}

	public static LocalDateTime toLocalDateTime(String dateTime)
	{
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateTimeFormat()));
	}

	public static ZonedDateTime toZonedDateTime(Object dateTime)
	{
		return toZonedDateTime(dateTime, null);
	}

	public static ZonedDateTime toZonedDateTime(Object dateTime, String zoneId)
	{
		if (dateTime == null)
		{
			return null;
		}

		ZonedDateTime zonedDateTime;

		if (dateTime instanceof LocalDateTime)
		{
			zonedDateTime = ZonedDateTime.of((LocalDateTime)dateTime, ZoneId.of(PlatformCommonVo.DEFAULT_TIMEZONE));
		}
		else
		{
			zonedDateTime = ZonedDateTime.parse((String)dateTime, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateTimeFormat()));
		}

		return zonedDateTime.withZoneSameInstant(getZoneId(zoneId));
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

	public static LocalDateTime parse(String date, String hours, String minutes, String seconds)
	{
		hours = CmStringUtils.defaultIfEmpty(CmStringUtils.leftPad(hours, 2, "0"), "00");
		minutes = CmStringUtils.defaultIfEmpty(CmStringUtils.leftPad(minutes, 2, "0"), "00");
		seconds = CmStringUtils.defaultIfEmpty(CmStringUtils.leftPad(seconds, 2, "0"), "00");

		return LocalDateTime.parse(date + hours + minutes + seconds, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateFormat() + "HHmmss"));
	}
}
