package org.jwebppy.platform.mgmt.i18n.dto;

public enum LangType
{
	L("LABEL"), B("BUTTON"), T("TEXT"), M("MESSAGE");

	private String type;

	private LangType(String type)
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
