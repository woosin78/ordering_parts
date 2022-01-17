package org.jwebppy.platform.core.dao.support;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;

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

	/*
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
	*/

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

	public void putDate(String key, Object value)
	{
		if (value instanceof CharSequence)
		{
			if (CmStringUtils.isNotEmpty(value))
			{
				String strValue = (String)value;
				String format = CmDateFormatUtils.getDateFormat();

				if (GenericValidator.isDate(strValue, format, true))
				{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

					try
					{
						this.put(key, new SimpleDateFormat(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD).format(new Date(simpleDateFormat.parse(strValue).getTime())));
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*
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
	*/

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
