package org.jwebppy.platform.mgmt.content.dto;

public enum CItemType
{
	P("PAGE"), M("MENU"), R("ROLE"), G("GROUP"), F("FOLDER");

	private String type;

	private CItemType(String type)
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
