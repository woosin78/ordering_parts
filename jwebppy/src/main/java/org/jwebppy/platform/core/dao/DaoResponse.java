package org.jwebppy.platform.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.IDataList;
import org.jwebppy.platform.core.dao.support.IDataMap;
import org.springframework.util.CollectionUtils;

public class DaoResponse extends AbstractDaoResponse
{
    private final Map<String, Object> resultMap = new HashMap<>();

    public DaoResponse() {}

    public DaoResponse(Map<String, Object> resultMap)
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

    public String getString(String name)
    {
    	return (String)getObject(name);
    }

    public IDataMap getStructure(String name)
    {
    	return new DataMap((HashMap)resultMap.get(name));
    }

    public IDataList getTable(String name)
    {
    	return new DataList((List)resultMap.get(name));
    }
}
