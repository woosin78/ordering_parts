package org.jwebppy.platform.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jwebppy.platform.core.PlatformCommonVo;

public class CmDateFormatUtils
{
	public static String format(LocalDateTime localDateTime)
	{
		return format(localDateTime, null);
	}

	public static String format(LocalDateTime localDateTime, String format)
	{
		if (localDateTime != null)
		{
			if (CmStringUtils.isEmpty(format))
			{
				return localDateTime.format(DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT));
			}
			else
			{
				return localDateTime.format(DateTimeFormatter.ofPattern(format));
			}
		}

		return null;
	}

	public static String now()
	{
		return format(LocalDateTime.now());
	}

	public static String now(String format)
	{
		return format(LocalDateTime.now(), format);
	}

	public static String today()
	{
		return format(LocalDateTime.now(), PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public static String plusDays(long days)
	{
		return format(LocalDateTime.now().plusDays(days), null);
	}

	public static String plusDays(int days, String format)
	{
		return format(LocalDateTime.now().plusDays(days), format);
	}

	public static String plusWeeks(long weeks)
	{
		return format(LocalDateTime.now().plusWeeks(weeks), null);
	}

	public static String plusWeeks(long weeks, String format)
	{
		return format(LocalDateTime.now().plusWeeks(weeks), format);
	}

	public static String plusMonths(long months)
	{
		return format(LocalDateTime.now().plusMonths(months), null);
	}

	public static String plusMonths(long months, String format)
	{
		return format(LocalDateTime.now().plusMonths(months), format);
	}

	public static String plusYears(long years)
	{
		return format(LocalDateTime.now().plusYears(years), null);
	}

	public static String plusYears(long years, String format)
	{
		return format(LocalDateTime.now().plusMonths(years), format);
	}

	public static String theFirstDateMonth(LocalDateTime localDateTime)
	{
		return format(localDateTime.withDayOfMonth(1), PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public static String theFirstDateThisMonth()
	{
		return theFirstDateMonth(LocalDateTime.now());
	}

	public static String unlimitDate(String format)
	{
		return format(LocalDateTime.parse(PlatformCommonVo.UNLIMITED_DATE_TIME, DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)), format);
	}

	public static String unlimitDate()
	{
		return unlimitDate(PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}
}
