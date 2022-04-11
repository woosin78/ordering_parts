package org.jwebppy.platform.mgmt.logging.dto;

public enum IfType
{
	J("JDBC"), R("RFC");

	private String type;

	private IfType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
