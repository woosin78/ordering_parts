package org.jwebppy.platform.core.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jwebppy.platform.core.PlatformConfigVo;

public class CmStringUtils extends StringUtils
{
	public static String trimToEmpty(Object obj)
	{
		if (obj == null)
		{
			return "";
		}

		return trimToEmpty(obj.toString());
	}

	public static boolean isEmpty(Object obj)
	{
		if (obj == null)
		{
			return true;
		}

		return isEmpty(obj.toString());
	}

	public static boolean isNotEmpty(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		return isNotEmpty(obj.toString());
	}

	public static boolean equals(Object obj1, Object obj2)
	{
		if (obj1 == null || obj2 == null)
		{
			return false;
		}

		return equals(obj1.toString(), obj2.toString());
	}

	public static boolean notEquals(Object obj1, Object obj2)
	{
		return !equals(obj1, obj2);
	}

	public static String defaultString(Object obj1)
	{
		if (isEmpty(obj1))
		{
			return "";
		}

		return trimToEmpty(obj1);
	}

	public static String defaultString(Object obj1, Object obj2)
	{
		if (isEmpty(obj1))
		{
			return obj2.toString();
		}

		return trimToEmpty(obj1);
	}

	public static String defaultIfEmpty(Object obj1, Object obj2)
	{
		if (isEmpty(obj1))
		{
			return obj2.toString();
		}

		return trimToEmpty(obj1);
	}

	public static String leftPad(Object obj, int size, String padStr)
	{
		return leftPad(defaultString(obj), size, padStr);
	}

	public static int indexOfIgnoreCaseAndEmpty(final CharSequence str1, final CharSequence str2)
	{
		if (isEmpty(str1) || isEmpty(str2))
		{
			return -1;
		}

		return indexOfIgnoreCase(str1, str2);
	}

	public static boolean containsIgnoreCaseAndEmpty(final CharSequence str1, final CharSequence str2)
	{
		if (isEmpty(str1) || isEmpty(str2))
		{
			return false;
		}

		return containsIgnoreCase(str1, str2);
	}

	public static <E> String arrayToString(List<E> list)
	{
		if (CollectionUtils.isNotEmpty(list))
		{
			StringBuilder result = new StringBuilder();

			for (int i=0, size=list.size(); i<size; i++)
			{
				result.append(list.get(i).toString());

				if (i < size-1)
				{
					result.append(PlatformConfigVo.DELIMITER);
				}
			}

			return result.toString();
		}

		return null;
	}

	public static String upperCase(Object str)
	{
		return upperCase((String)str);
	}

	public static String lowerCase(Object str)
	{
		return lowerCase((String)str);
	}
}
