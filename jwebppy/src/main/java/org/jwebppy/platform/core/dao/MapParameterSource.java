package org.jwebppy.platform.core.dao;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.IDataHandle;
import org.jwebppy.platform.core.dao.support.IDataMap;
import org.springframework.util.CollectionUtils;

public class MapParameterSource extends AbstractParameterSource implements IDataHandle
{
	private final IDataMap valueMap = new DataMap();

	public MapParameterSource()
	{
		super();
	}

	public MapParameterSource(String name, Object value)
	{
		addValue(name, value);
	}

	public MapParameterSource(Map valueMap)
	{
		addValues(valueMap);
	}

	public boolean containsKey(Object key)
	{
		return valueMap.containsKey(key);
	}

	public MapParameterSource addValue(String name, Object value)
	{
	    if (name != null && value != null)
	    {
	        valueMap.put(name, value);

	        if (value instanceof Parameter)
	        {
	            registerType(name, ((Parameter)value).getType());
	            registerTypeName(name, ((Parameter)value).getTypeName());
	        }
	    }

	    return this;
	}

	public void addValues(Map valueMap)
	{
		if (!CollectionUtils.isEmpty(valueMap))
		{
			Iterator it = valueMap.keySet().iterator();
			String key = null;

			while (it.hasNext())
			{
				key = (String)it.next();
				addValue(key, valueMap.get(key));
			}
		}
	}

	@Override
	public Object getValue(String name)
	{
		return valueMap.get(name);
	}

	@Override
	public boolean hasValue(String name)
	{
		return valueMap.containsKey(name);
	}

	public Map getValues()
	{
		return Collections.unmodifiableMap(valueMap);
	}

	@Override
	public String getString(Object name)
	{
		return valueMap.getString(name);
	}

	@Override
	public String getString(Object name, String defaultValue)
	{
		return valueMap.getString(name, defaultValue);
	}

	@Override
	public byte[] getBytes(Object name)
	{
		return valueMap.getBytes(name);
	}

	@Override
	public int getInt(Object name)
	{
		return valueMap.getInt(name);
	}

	@Override
	public int getInt(Object name, int defaultValue)
	{
		return valueMap.getInt(name, defaultValue);
	}

	@Override
	public long getLong(Object name)
	{
		return valueMap.getLong(name);
	}

	@Override
	public long getLong(Object name, long defaultValue)
	{
		return valueMap.getLong(name, defaultValue);
	}

	@Override
	public float getFloat(Object name)
	{
		return valueMap.getFloat(name);
	}

	@Override
	public float getFloat(Object name, float defaultValue)
	{
		return valueMap.getFloat(name, defaultValue);
	}

	@Override
	public double getDouble(Object name)
	{
		return valueMap.getDouble(name);
	}

	@Override
	public double getDouble(Object name, double defaultValue)
	{
		return valueMap.getDouble(name, defaultValue);
	}

	@Override
	public BigDecimal getDecimal(Object name)
	{
		return valueMap.getDecimal(name);
	}

	@Override
	public BigDecimal getDecimal(Object name, BigDecimal defaultValue)
	{
		return valueMap.getDecimal(name, defaultValue);
	}

	@Override
	public Date getDate(Object name)
	{
		return valueMap.getDate(name);
	}

	@Override
	public Time getTime(Object name)
	{
		return valueMap.getTime(name);
	}

	@Override
	public Timestamp getTimestamp(Object name)
	{
		return valueMap.getTimestamp(name);
	}

	@Override
	public Object getObject(Object name)
	{
		return valueMap.getObject(name);
	}

	@Override
	public boolean isNullValue(Object name)
	{
		return valueMap.isNullValue(name);
	}

	@Override
	public boolean isEmptyValue(Object name)
	{
		return valueMap.isEmptyValue(name);
	}

	@Override
	public boolean isNotEmptyValue(Object name)
	{
		return !isEmptyValue(name);
	}

	@Override
	public boolean isEquals(Object name, Object value)
	{
		return valueMap.isEquals(name, value);
	}
}
