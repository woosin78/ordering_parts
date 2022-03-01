package org.jwebppy.platform.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.Dialect;

public class CmMyBatisQueryUtils
{
	public static boolean isEmpty(Object obj)
	{
		if (obj instanceof Map)
		{
			return MapUtils.isEmpty((Map<?, ?>)obj);
		}
		else if (obj instanceof List)
		{
			return CollectionUtils.isEmpty((List<?>)obj);
		}

		return CmStringUtils.isEmpty(obj);
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
	 * reg_date>'20220301' and reg_date<'20220302'
	 */
	public static String dateFormat(String date, String type)
	{
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateFormat()));

		if (CmStringUtils.equals(type, "TO"))
		{
			localDate = localDate.plusDays(1);
		}

		return localDate.format(DateTimeFormatter.ofPattern(IvCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD));
	}
}
