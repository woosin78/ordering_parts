package org.jwebppy.platform.core.dao.support;

import java.io.Serializable;

import org.jwebppy.platform.core.util.CmStringUtils;

public class ErpDataMap extends DataMap implements Serializable
{
	private static final long serialVersionUID = -2321068482595618638L;

	public ErpDataMap()
	{
		super();
	}

	public ErpDataMap(Object obj)
	{
		super(obj);
	}

	public String getCorpNo()
	{
		return getString("BUKRS");
	}

	public String getCorpName()
	{
		if (isEquals("BUKRS", "7800"))
		{
			return "DIVEU";
		}
		else if (isEquals("BUKRS", "7200"))
		{
			return "DIVUK";
		}

		return "";
	}

	public String getUsername()
	{
		return getString("BNAME");
	}

	public String getSalesOrg()
	{
		return getString("VKORG");
	}

	public String getDistChannel()
	{
		return getString("VTWEG");
	}

	public String getDivision()
	{
		return getString("SPART");
	}

	public String getCustomerNo()
	{
		return getString("KUNNR");
	}

	public String getCustomerName()
	{
		return getString("NAME1");
	}

	public String getLang()
	{
		return getString("LANG");
	}

	public String getLandscape()
	{
		if (isEquals("BUKRS", "7800"))
		{
			return "P01";
		}
		else if (isEquals("BUKRS", "7200"))
		{
			return "GMT";
		}

		return "P09";
	}

	public boolean isAnyEmptyValue(String[] keys)
	{
		if (keys != null)
		{
			for (String key : keys)
			{
				if (CmStringUtils.isEmpty(key))
				{
					return true;
				}
			}
		}

		return false;
	}
}
