package org.jwebppy.platform.core.dao.support;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.GenericValidator;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;

public class ErpDataMap extends DataMap implements Serializable
{
	private static final long serialVersionUID = -2321068482595618638L;

	private Map map = null;

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

	public ErpDataMap with(Map map)
	{
		this.map = map;

		return this;
	}

	public ErpDataMap addByKey(String to, String from)
	{
		if (MapUtils.isNotEmpty(map))
		{
			this.put(to, this.map.get(from));
		}

		return this;
	}

	public ErpDataMap addByKey(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (CmStringUtils.isNotEmpty(field[0]) && CmStringUtils.isNotEmpty(field[1]))
					{
						this.put(field[0], this.map.get(field[1]));
					}
				}
			}
		}

		return this;
	}

    public ErpDataMap add(String key, Object value)
    {
    	this.put(key, value);

    	return this;
    }

	public ErpDataMap add(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (CmStringUtils.isNotEmpty(field[0]) && CmStringUtils.isNotEmpty(field[1]))
					{
						this.put(field[0], field[1]);
					}
				}
			}
		}

		return this;
	}

	public ErpDataMap addDate(String key, Object value)
	{
		addDateToMap(key, value);

		return this;
	}

	public ErpDataMap addDate(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (CmStringUtils.isNotEmpty(field[0]) && CmStringUtils.isNotEmpty(field[1]))
					{
						addDateToMap((String)field[0], (String)field[1]);
					}
				}
			}
		}

		return this;
	}

	public ErpDataMap addDateByKey(String to, String from)
	{
		addDateToMap(to, this.map.get(from));

		return this;
	}

	public ErpDataMap addDateByKey(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (CmStringUtils.isNotEmpty(field[0]) && CmStringUtils.isNotEmpty(field[1]))
					{
						addDateToMap((String)field[0], this.map.get(field[1]));
					}
				}
			}
		}

		return this;
	}

	public void addDateToMap(String key, Object value)
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

	public boolean hasEmptyValue(String[] keys)
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
