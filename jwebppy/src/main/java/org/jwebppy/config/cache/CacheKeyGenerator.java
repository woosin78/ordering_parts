package org.jwebppy.config.cache;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.interceptor.KeyGenerator;

public class CacheKeyGenerator implements KeyGenerator
{
	@Override
	public Object generate(Object target, Method method, Object... params)
	{

		System.err.println("============================================================");
		System.err.println("target:" + target.getClass().getName() + ", method:" + method.getName());

		StringBuffer key = new StringBuffer();
		key.append(target.getClass().getName());
		key.append(method.getName());

		if (ArrayUtils.isNotEmpty(params))
		{
			for (Object param: params)
			{
				System.err.println(param.getClass().getName());

				if (ObjectUtils.isNotEmpty(key))
				{
					key.append(param.toString());
				}
			}
		}
		System.err.println("============================================================");

		return key.toString();
	}

}
