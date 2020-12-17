package org.jwebppy.platform.core.dao.sap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.AbstractDaoResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.springframework.util.CollectionUtils;

public class RfcResponse extends AbstractDaoResponse implements Serializable
{
	private static final long serialVersionUID = -7318442099344682977L;

	private final Map resultMap = new HashMap();

    public RfcResponse() {}

    public RfcResponse(Map resultMap)
    {
    	if (!CollectionUtils.isEmpty(resultMap))
    	{
    		this.resultMap.putAll(resultMap);
    	}
    }

    public Object getObject(String name)
    {
    	return resultMap.get(name);
    }

    public void setObject(String name, Object value)
    {
    	resultMap.put(name, value);
    }

    public String getString(String name)
    {
    	return (String)getObject(name);
    }

    public DataMap getStructure(String name)
    {
    	return new DataMap((HashMap)resultMap.get(name));
    }

    public DataList getTable(String name)
    {
    	return new DataList((List)resultMap.get(name));
    }

    @Override
	public String toString()
    {
    	Iterator iterator = resultMap.entrySet().iterator();

    	while (iterator.hasNext())
    	{
    		iterator.next();

    	}

    	return "";
    }
}
