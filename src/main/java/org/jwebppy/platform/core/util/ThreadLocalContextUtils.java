package org.jwebppy.platform.core.util;

import org.jwebppy.platform.core.dto.ServletRequestInfoDto;

public class ThreadLocalContextUtils
{
	public static ThreadLocal<ServletRequestInfoDto> servletRequestInfo = new ThreadLocal<ServletRequestInfoDto>();
}
