package org.jwebppy.platform.mgmt.logging.dto;

public enum ParameterType
{
	F("FIELD"), S("STURUCTURE"), T("TABLE");

	private String type;

	private ParameterType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
