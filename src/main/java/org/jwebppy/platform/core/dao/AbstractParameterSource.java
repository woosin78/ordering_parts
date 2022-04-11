package org.jwebppy.platform.core.dao;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractParameterSource implements IParameterSource
{
	public int TYPE_UNKNOWN = -2147483648;
	private Map typeMap = new HashMap();
	private Map typeNameMap = new HashMap();

	public void registerType(String name, int type)
	{
		typeMap.put(name, new Integer(type));
	}

	public void registerTypeName(String name, String typeName)
	{
		typeNameMap.put(name, typeName);
	}

	@Override
	public int getType(String name)
	{
		Integer type = (Integer)typeMap.get(name);

		if (type == null)
		{
			return TYPE_UNKNOWN;
		}

		return type.intValue();
	}

	@Override
	public String getTypeName(String name)
	{
		return (String)typeNameMap.get(name);
	}
}
