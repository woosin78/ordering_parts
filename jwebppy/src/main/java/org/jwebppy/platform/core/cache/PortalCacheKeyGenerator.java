package org.jwebppy.platform.core.cache;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

public class PortalCacheKeyGenerator implements KeyGenerator
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
		key.append(salesOrg).append(":").append(custCode).append(":");

		for (Object param: params)
		{
			if (ObjectUtils.isNotEmpty(param))
			{
				key.append(param.toString());
			}
		}

		return key.toString();
	}
}
