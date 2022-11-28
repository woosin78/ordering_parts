package org.jwebppy.platform.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.portal.common.PortalCommonVo;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.Dialect;

public class CmMyBatisQueryUtils
{
	public static boolean isEmpty(Object obj)
	{
		return ObjectUtils.isEmpty(obj);
	}

	public static boolean isNotEmpty(Object obj)
	{
		return !isEmpty(obj);
	}

	public static boolean equals(Object obj1, Object obj2)
	{
		return CmStringUtils.equals(obj1, obj2);
	}

	public static boolean notEquals(Object obj1, Object obj2)
	{
		return CmStringUtils.notEquals(obj1, obj2);
	}

	public static String format(String sql)
	{
		return SqlFormatter
				.of(Dialect.MariaDb)
				.format(CmStringUtils.trimToEmpty(sql),
						FormatConfig.builder()
						.maxColumnLength(200)
						.build());
	}

	/*
	 * reg_date>='20220301' and reg_date<'20220302'
	 */
	public static String dateFormat(String date, String type)
	{
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateFormat()));

		if (CmStringUtils.equals(type, "TO"))
		{
			localDate = localDate.plusDays(1);
		}

		return localDate.format(DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD));
	}

	public static LocalDateTime toDate(String date, String type)
	{
		String formattedDate = dateFormat(date, type) + "000000";

		return LocalDateTime.parse(formattedDate, DateTimeFormatter.ofPattern(PortalCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS));
	}
}
