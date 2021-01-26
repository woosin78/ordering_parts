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

	public static CItemType[] values2()
	{
		CItemType[] cItemTypes = new CItemType[CItemType.values().length-1];
		int index = 0;

		for (CItemType cItemType: CItemType.values())
		{
			if (cItemType.equals(CItemType.G))
			{
				continue;
			}

			cItemTypes[index++] = cItemType;
		}

		return cItemTypes;
	}
}
