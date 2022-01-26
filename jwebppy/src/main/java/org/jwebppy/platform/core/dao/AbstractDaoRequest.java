package org.jwebppy.platform.core.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jwebppy.platform.core.dao.sap.BeanPropertyParameterSource;
import org.jwebppy.platform.core.dao.sap.JCoMapRowMapper;
import org.jwebppy.platform.core.dao.sap.RowMapper;
import org.jwebppy.platform.core.dao.support.EasyCollectionFactory;
import org.jwebppy.platform.core.util.CmStringUtils;

public abstract class AbstractDaoRequest implements IDaoRequest
{
	private MapParameterSource mapParameterSource = new MapParameterSource();
	private Map outputParameterMap = new LinkedHashMap();
	private Class rowMapperType;

	public AbstractDaoRequest() {}

	public AbstractDaoRequest(Object param)
	{
		addParameters(param);
	}

	@Override
	public void addParameter(String name, Object value)
	{
		if (CmStringUtils.isNotEmpty(name) && value != null)
		{
			mapParameterSource.addValue(name, value);
		}
	}

	public void addParameters(Object param)
	{
        if (param != null)
        {
        	Map paramMap = null;

            if (EasyCollectionFactory.isApproximableMapType(param))
            {
            	paramMap = (Map)param;
            }
            else
            {
            	paramMap = new BeanPropertyParameterSource(param).asMap();
            }

            if (paramMap != null)
            {
            	Iterator it = paramMap.keySet().iterator();
            	String key = null;

            	while (it.hasNext())
            	{
            		key = (String)it.next();
            		addParameter(key, paramMap.get(key));
            	}
            }
        }
	}

	@Override
	public Object getParameter(String name)
	{
		return mapParameterSource.getValue(name);
	}

	@Override
	public void addOutputParameter(OutputParameter param)
	{
		outputParameterMap.put(param.getName(), param);
	}

	public void addOutputParameter(String name)
	{
		addOutputParameter(new OutputParameter(name, new JCoMapRowMapper()));
	}

	public void addOutputParameter(String name, RowMapper rowMapper)
	{
		addOutputParameter(new OutputParameter(name, rowMapper));
	}

	@Override
	public Map getOutputParameterMap()
	{
		return outputParameterMap;
	}

	@Override
	public void setRowMapperType(Class rowMapperType)
	{
		this.rowMapperType = rowMapperType;
	}

	@Override
	public Class getRowMapperType()
	{
		if (rowMapperType == null)
		{
		    return HashMap.class;
		}

		return rowMapperType;
	}

	public MapParameterSource getParameterSource()
	{
		return mapParameterSource;
	}

	public void addField(String name, Object value)
	{
		addParameter(name, new ParameterValue(name, DataType.FIELD, value));
	}

	public void addField(Map<String, Object> fieldMap)
	{
		if (MapUtils.isNotEmpty(fieldMap))
	    {
	        Iterator<String> it = fieldMap.keySet().iterator();
	        String name = null;

	        while (it.hasNext())
	        {
	            name = it.next();

	            addField(name, fieldMap.get(name));
	        }
	    }
	}

	public void addStructure(String name, String fieldName, Object value)
	{
	    if (CmStringUtils.isNotEmpty(name) && CmStringUtils.isNotEmpty(fieldName) && value != null)
	    {
            ParameterValue paramValue = (ParameterValue)getParameter(name);

            if (paramValue == null)
            {
                MapParameterSource paramSource = new MapParameterSource();
                paramSource.addValue(fieldName, new ParameterValue(fieldName, value));

                addParameter(name, new ParameterValue(name, DataType.STRUCTURE, paramSource));
            }
            else
            {
                MapParameterSource paramSource = (MapParameterSource)paramValue.getValue();
                paramSource.addValue(fieldName, new ParameterValue(fieldName, value));
            }
	    }
	}

	public void addStructure(String name, Object value)
	{
	    if (CmStringUtils.isNotEmpty(name) && value != null)
	    {
	        Map<String, Object> paramMap = null;

	        if (EasyCollectionFactory.isApproximableMapType(value))
	        {
	            paramMap = (Map<String, Object>)value;
	        }
	        else
	        {
	            paramMap = new BeanPropertyParameterSource(value).asMap();
	        }

	        Iterator<String> it = paramMap.keySet().iterator();
	        String fieldName = null;

	        while (it.hasNext())
	        {
	        	fieldName = it.next();

	            addStructure(name, fieldName, paramMap.get(fieldName));
	        }
	    }
	}

    public void addStructure(Map structureMap)
    {
    	if (MapUtils.isNotEmpty(structureMap))
        {
        	Iterator it = structureMap.entrySet().iterator();
            Entry et = null;

            while (it.hasNext())
            {
            	et = (Entry)it.next();

            	addStructure((String)et.getKey(), et.getValue());
            }
        }
    }

	public void addTable(String name, List valueList)
	{
		if (CmStringUtils.isNotEmpty(name) && CollectionUtils.isNotEmpty(valueList))
		{
			ParameterValue paramValue = (ParameterValue)getParameter(name);
			MapParameterSource paramSource = null;

			if (paramValue == null)
			{
				paramSource = new MapParameterSource();
				paramValue = new ParameterValue(name, DataType.TABLE, paramSource);

				addParameter(name, paramValue);
			}
			else
			{
				paramSource = (MapParameterSource)paramValue.getValue();
			}

	    	Map paramMap = null;
	    	Object value = null;
	    	String index = null;
	    	int size = paramSource.getValues().size();

	    	for (int i=0,size2=valueList.size(); i<size2; i++)
	    	{
	    		value = valueList.get(i);

	    		if (value == null)
	    		{
	    			continue;
	    		}

		        if (EasyCollectionFactory.isApproximableMapType(value))
		        {
		        	paramMap = (Map)value;
		        }
		        else
		        {
		        	paramMap = new BeanPropertyParameterSource(value).asMap();
		        }

		        index = Integer.toString(i+size);

		        paramSource.addValue(index, new ParameterValue(index, paramMap));
	    	}
		}
	}

	public void addTable(String name, Map valueMap)
	{
		if (MapUtils.isNotEmpty(valueMap))
		{
			List<Map<String, Object>> tableList = new LinkedList<>();
			tableList.add(valueMap);

			addTable(name, tableList);
		}
	}

	public void addTable(Map tableMap)
	{
	    if (MapUtils.isNotEmpty(tableMap))
	    {
	        Iterator it = tableMap.entrySet().iterator();
	        Entry et = null;

	        while (it.hasNext())
	        {
	            et = (Entry)it.next();

	            addTable((String)et.getKey(), (List)et.getValue());
	        }
	    }
	}

	@Override
	public String toString()
	{
		return "AbstractDaoRequest [mapParameterSource=" + mapParameterSource + ", outputParameterMap="
				+ outputParameterMap + ", rowMapperType=" + rowMapperType + "]";
	}
}
