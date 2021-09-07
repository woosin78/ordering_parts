package org.jwebppy.platform.core.util;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.Dialect;

public class CmMyBatisQueryUtils
{
	public static boolean isEmpty(Object obj)
	{
		return CmStringUtils.isEmpty(obj);
	}

	public static boolean isNotEmpty(Object obj)
	{
		return CmStringUtils.isNotEmpty(obj);
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
}
