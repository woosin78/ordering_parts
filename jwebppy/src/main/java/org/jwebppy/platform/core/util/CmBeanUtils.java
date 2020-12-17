package org.jwebppy.platform.core.util;

import org.springframework.beans.BeanUtils;

public class CmBeanUtils extends BeanUtils
{
	public static boolean isNotSimpleValueType(Class<?> clazz)
	{
		return !isSimpleValueType(clazz);
	}
}
