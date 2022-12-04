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
		List<CItemType> citemTypes = new ArrayList<>();

		for (CItemType citemType: CItemType.values())
		{
			if (citemType.equals(CItemType.G))
			{
				continue;
			}

			citemTypes.add(citemType);
		}

		return citemTypes;
	}

	public static List<CItemType> availableTypes(CItemType citemType)
	{
		List<CItemType> citemTypes = new ArrayList<>();

		if (CItemType.F.equals(citemType))
		{
			return values2();
		}
		else if (CItemType.R.equals(citemType) || CItemType.M.equals(citemType))
		{
			citemTypes.add(CItemType.P);
			citemTypes.add(CItemType.M);
		}

		return citemTypes;
	}
}
