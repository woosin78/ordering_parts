package org.jwebppy.platform.core.security.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jwebppy.platform.core.util.CmStringUtils;

public enum AuthenticationType
{
	N("NORMAL"), A("AD"), S("SSO"), D("DOOBIZ");

	private String type;
	private Map<String, String> typeMap = new HashMap<>();

	{
		typeMap.put("NOMAL", "DOOBIZPLUS-USER");
		typeMap.put("AD", "AD-USER");
		typeMap.put("SSO", "SSO-USER");
		typeMap.put("DOOBIZ", "DOOBIZ-USER");
	}

	private AuthenticationType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public String getUniqueName()
	{
		return typeMap.get(type);
	}

	public AuthenticationType valueOfByUniqueName(String type)
	{
		for (Entry<String, String> entry : typeMap.entrySet())
		{
			if (CmStringUtils.equals(entry.getValue(), type))
			{
				return AuthenticationType.valueOf(entry.getKey());
			}
		}

		return null;
	}
}
