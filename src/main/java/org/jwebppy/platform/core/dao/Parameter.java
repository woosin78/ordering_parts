package org.jwebppy.platform.core.dao;

public class Parameter
{
	private String name;
	private int type;
	private String typeName;

	public Parameter(String name)
	{
		this.name = name;
	}

	public Parameter(String name, int type)
	{
		this.name = name;
		this.type = type;
	}

	public Parameter(String name, int type, String typeName)
	{
		this.name = name;
		this.type = type;
		this.typeName = typeName;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getTypeName()
	{
		return this.typeName;
	}

	@Override
	public String toString()
	{
		return "Parameter [name=" + name + ", type=" + type + ", typeName=" + typeName + "]";
	}
}
