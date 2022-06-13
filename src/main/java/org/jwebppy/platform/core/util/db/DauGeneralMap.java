package org.jwebppy.platform.core.util.db;

import java.util.HashMap;
import java.util.Map;

public class DauGeneralMap extends HashMap
{
    public DauGeneralMap() {}

    public DauGeneralMap(Map map)
    {
    	if (map != null)
    	{
    		putAll(map);
    	}
    }

    public void putRemoveNull(Object key, Object initValue)
    {
        Object value = get(key);

        if (value == null)
        {
            put(key, initValue);
        }
        else
        {
            if (value instanceof String || value instanceof StringBuffer)
            {
                if ("".equals(value.toString().trim()))
                {
                    put(key, initValue);
                }
            }
        }
    }

    public void putCopyValue(Object sourceKey, Object targetKey)
    {
    	put(targetKey, get(sourceKey));
    }

    public void replaceAll(String key, String regex, String replacement)
    {
        put(key, getString(key).replaceAll(regex, replacement));
    }

    public void toUpperCase(String key)
    {
        put(key, getString(key).toUpperCase());
    }

    public void toLowerCase(String key)
    {
        put(key, getString(key).toLowerCase());
    }

    public String getString(String key)
    {
        return getString(key, "");
    }

    public String getString(String key, String initValue)
    {
        String value = (String)get(key);
        if (value == null || "".equals(value.trim()))
        {
            return initValue;
        }

        return value.trim();
    }

    public byte[] getBytes(String key)
    {
        return getString(key).getBytes();
    }

    public short getShort(String key)
    {
        return Short.parseShort(getString(key, "0"));
    }

    public int getInt(String key)
    {
        return Integer.parseInt(getString(key, "0"));
    }

    public long getLong(String key)
    {
        return Long.parseLong(getString(key, "0"));
    }

    public float getFloat(String key)
    {
        return Float.parseFloat(getString(key, "0"));
    }

    public double getDouble(String key)
    {
        return Double.parseDouble(getString(key, "0"));
    }

    public boolean isEmptyValue(String key)
    {
        Object obj = get(key);

        if (obj instanceof String || obj instanceof StringBuffer)
        {
            return ("".equals(getString(key)))?true:false;
        }
        else
        {
            return (get(key) == null)?true:false;
        }
    }

    public boolean isEquals(String key, Object value)
    {
        if (value == null)
        {
            return false;
        }

        if (value.equals(get(key)))
        {
            return true;
        }
        return false;
    }
}
