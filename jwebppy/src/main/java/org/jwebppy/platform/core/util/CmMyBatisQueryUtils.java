package org.jwebppy.platform.core.util;

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
}
