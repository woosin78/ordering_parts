package org.jwebppy.platform.core.dao;

public interface IParameterSource
{
	public Object getValue(String name);
	public boolean hasValue(String name);
	public int getType(String name);
	public String getTypeName(String name);
}
