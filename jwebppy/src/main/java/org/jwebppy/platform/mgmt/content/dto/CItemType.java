package org.jwebppy.platform.mgmt.content.dto;

import java.util.ArrayList;
import java.util.List;

public enum CItemType
{
	P("PAGE"), M("MENU"), R("ROLE"), F("FOLDER"), G("GROUP");

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

	public static List<CItemType> values2()
	{
		List<CItemType> cItemTypes = new ArrayList<>();

		for (CItemType cItemType: CItemType.values())
		{
			if (cItemType.equals(CItemType.G))
			{
				continue;
			}

			cItemTypes.add(cItemType);
		}

		return cItemTypes;
	}

	public static List<CItemType> availableTypes(CItemType cItemType)
	{
		List<CItemType> cItemTypes = new ArrayList<>();

		if (CItemType.F.equals(cItemType))
		{
			return values2();
		}
		else if (CItemType.R.equals(cItemType) || CItemType.M.equals(cItemType))
		{
			cItemTypes.add(CItemType.P);
			cItemTypes.add(CItemType.M);
		}

		return cItemTypes;
	}
}
