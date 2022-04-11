package org.jwebppy.platform.core.util;

import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;

public class JdbcStatementContextUtils
{
	private static final ThreadLocal<DataAccessLogDto> threadLocal = new ThreadLocal<>();

	public static DataAccessLogDto get()
	{
		return threadLocal.get();
	}

	public static void put(DataAccessLogDto dataAccessLogDto)
	{
		threadLocal.set(dataAccessLogDto);
	}
}
