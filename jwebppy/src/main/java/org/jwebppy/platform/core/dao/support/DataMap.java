package org.jwebppy.platform.core.dao.support;

import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.util.CollectionUtils;

public class DataMap implements IDataMap, Serializable
{
	private static final long serialVersionUID = -8935391122861637143L;

	private Map map = null;

	public DataMap()
	{
		map = new LinkedHashMap();
	}

	public DataMap(Object obj)
	{
		if (obj != null && obj instanceof Map)
		{
			this.map = (Map)obj;
		}
	}

	@Override
	public int size()
	{
		return map.size();
	}

	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key)
	{
		return map.get(key);
	}

	@Override
	public Object put(Object key, Object value)
	{
		if (value instanceof CharSequence)
		{
			value = CmStringUtils.trimToEmpty(value);
		}

		return map.put(key, value);
	}

	@Override
	public Object remove(Object key)
	{
		return map.remove(key);
	}

	@Override
	public void putAll(Map t)
	{
		map.putAll(t);
	}

	@Override
	public void clear()
	{
		map.clear();
	}

	@Override
	public Set<Object> keySet()
	{
		return map.keySet();
	}

	@Override
	public Collection<Object> values()
	{
		return map.values();
	}

	@Override
	public Set<?> entrySet()
	{
		return map.entrySet();
	}

	@Override
	public String toString()
	{
		return map.toString();
	}

    @Override
	public Iterator<Object> iteratorByKeySet()
    {
    	return map.keySet().iterator();
    }

    @Override
	public Iterator<?> iteratorByEntrySet()
    {
    	return map.entrySet().iterator();
    }

	@Override
	public String getString(Object key)
    {
    	return getString(key, "");
    }

    @Override
	public String getString(Object key, String defaultValue)
    {
    	Object value = get(key);

    	if (value == null)
    	{
    		return defaultValue;
    	}

        return CmStringUtils.trimToEmpty(value);
    }

    @Override
	public byte[] getBytes(Object key)
    {
        return getString(key).getBytes();
    }

    @Override
	public int getInt(Object key)
    {
    	return getInt(key, 0);
    }

    @Override
	public int getInt(Object key, int defaultValue)
    {
    	return getNumber(key, new Integer(defaultValue)).intValue();
    }

    @Override
	public long getLong(Object key)
    {
    	return getLong(key, 0L);
    }

    @Override
	public long getLong(Object key, long defaultValue)
    {
        return getNumber(key, new Long(defaultValue)).longValue();
    }

    @Override
	public float getFloat(Object key)
    {
    	return getFloat(key, 0.0F);
    }

    @Override
	public float getFloat(Object key, float defaultValue)
    {
        return getNumber(key, new Float(defaultValue)).floatValue();
    }

    @Override
	public double getDouble(Object key)
    {
    	return getDouble(key, 0.0D);
    }

    @Override
	public double getDouble(Object key, double defaultValue)
    {
        return getNumber(key, new Double(defaultValue)).doubleValue();
    }

	@Override
	public BigDecimal getDecimal(Object key)
	{
		return getDecimal(key, new BigDecimal("0"));
	}

    @Override
	public BigDecimal getDecimal(Object key, BigDecimal defaultValue)
    {
    	return (BigDecimal)getNumber(key, defaultValue);

    }

    public Number getNumber(Object key, Number defaultValue)
    {
    	BigDecimal value = null;

    	try
    	{
    		value = (BigDecimal)get(key);
    	}
    	catch (ClassCastException e)
    	{
    		throw e;
    	}

    	if (value == null)
    	{
    		return defaultValue;
    	}

    	return value;
    }

    @Override
	public Date getDate(Object key)
    {
    	return (Date)map.get(key);
    }

    @Override
	public Time getTime(Object key)
    {
    	return (Time)map.get(key);
    }

    @Override
	public Timestamp getTimestamp(Object key)
    {
    	return (Timestamp)map.get(key);
    }

    @Override
	public IDataMap getMap(Object key)
    {
        return new DataMap(map.get(key));
    }

    @Override
	public IDataList getList(Object key)
    {
        return new DataList((List<?>)map.get(key));
    }

    @Override
	public Object getObject(Object key)
    {
        return map.get(key);
    }

    @Override
	public boolean isNullValue(Object key)
    {
    	Object value = map.get(key);

    	return ((value == null) ? true : false);
    }

    @Override
	public boolean isEmptyValue(Object key)
    {
    	Object value = map.get(key);

    	if (EasyCollectionFactory.isApproximableCollectionType(value) || EasyCollectionFactory.isApproximableMapType(value))
    	{
    		return CollectionUtils.isEmpty((Collection)value);
    	}

    	return CmStringUtils.isEmpty(getString(key));
    }

    @Override
	public boolean isNotEmptyValue(Object key)
    {
    	return !isEmptyValue(key);
    }

    @Override
	public boolean isEquals(Object key, Object value)
    {
    	if (isNullValue(key))
    	{
    		return false;
    	}

        if (value.equals(get(key)))
        {
            return true;
        }

        return false;
    }

	public boolean isNotEquals(Object key, Object value)
    {
		return !isEquals(key, value);
    }

    public void putDefaultIfNull(Object key, Object defaultValue)
    {
    	putDefaultIfNull(key, get(key), defaultValue);
    }

    public void putDefaultIfNull(Object key, Object value, Object defaultValue)
    {
    	if (value == null && defaultValue != null)
    	{
    		map.put(key, defaultValue);
    	}
    	else
    	{
    		map.put(key, value);
    	}
    }

    public void putCopyValue(Object sourceKey, Object targetKey)
    {
        put(targetKey, get(sourceKey));
    }

    public void replaceAllIfString(String key, String regex, String replacement)
    {
        Object value = get(key);
        if (value != null && (value instanceof String || value instanceof StringBuffer || value instanceof StringWriter))
        {
        	put(key, value.toString().replaceAll(regex, replacement));
        }
    }

    public void toUpperCaseIfString(String key)
    {
        put(key, getString(key).toUpperCase());
    }

    public void toLowerCaseIfString(String key)
    {
        put(key, getString(key).toLowerCase());
    }
}
