package org.jwebppy.platform.core.dao;

public class ParameterValue extends Parameter
{
	private Object value;

	public ParameterValue(String name, Object value)
	{
		super(name);
		this.value = value;
	}

	public ParameterValue(String name, int type, Object value)
	{
		super(name, type);
		this.value = value;
	}

	public ParameterValue(String name, int type, String typeName, Object value)
	{
		super(name, type, typeName);
		this.value = value;
	}

	public ParameterValue(Parameter param, Object value)
	{
		super(param.getName(), param.getType(), param.getTypeName());
		this.value = value;
	}

	public Object getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return "ParameterValue [value=" + value + ", getName()=" + getName() + ", getType()=" + getType()
				+ ", getTypeName()=" + getTypeName() + ", toString()=" + super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}
}
