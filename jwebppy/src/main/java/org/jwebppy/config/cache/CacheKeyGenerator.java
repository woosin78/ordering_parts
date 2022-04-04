package org.jwebppy.config.cache;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

public class CacheKeyGenerator implements KeyGenerator
{
	@Override
	public Object generate(Object target, Method method, Object... params)
	{
		if (ArrayUtils.isEmpty(params))
		{
			return SimpleKey.EMPTY;
		}

		ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();

		String salesOrg = erpUserContext.getSalesOrg();
		String custCode = erpUserContext.getCustCode();

		StringBuilder key = new StringBuilder();
		key.append(salesOrg).append(":");
		key.append(custCode).append(":");

		for (Object param: params)
		{
			System.err.println(param.getClass().getName());

			if (ObjectUtils.isNotEmpty(key))
			{
				key.append(param.toString());
			}
		}

		return key.toString();
	}

}
