package org.jwebppy.platform.core.dao;

import org.jwebppy.platform.core.dao.sap.RowMapper;

public class OutputParameter extends Parameter
{
	private Class clazz = null;
	private RowMapper rowMapper = null;

	public OutputParameter(String name, int type)
	{
		super(name, type);
	}

	public OutputParameter(String name, RowMapper rowMapper)
	{
		super(name);
		this.rowMapper = rowMapper;
	}

	public OutputParameter(String name, int type, String typeName)
	{
		super(name, type, typeName);
	}

	public OutputParameter(String name, int type, String typeName, Class clazz)
	{
		super(name, type, typeName);
		this.clazz = clazz;
	}

	public Class getOutputType()
	{
		return clazz;
	}

	public RowMapper getRowMapper()
	{
		return rowMapper;
	}
}
