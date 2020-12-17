package org.jwebppy.platform.core.util;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

public class CmReflectionUtils extends ReflectionUtils
{
	public static Method findMethod(Class<?> clazz, String name, Object[] args)
	{
		return findMethod(clazz, name, toClass(args));
	}

	public static Class<?>[] toClass(Object[] args)
	{
		if (args != null)
		{
			Class<?>[] classes = new Class[args.length];
			int index = 0;

			for (Object arg : args)
			{
				classes[index++] = (arg == null) ? null : arg.getClass();
			}

			return classes;
		}

		return null;
	}
}
