package org.jwebppy.platform.core.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jwebppy.platform.core.dao.sap.BeanPropertyParameterSource;
import org.jwebppy.platform.core.dao.support.EasyCollectionFactory;
import org.springframework.util.CollectionUtils;

public class DaoRequest extends AbstractDaoRequest
{
	@Override
	public void addField(String name, Object value)
	{
		addParameter(name, new ParameterValue(name, DataType.FIELD, value));
	}

	@Override
	public void addField(Map fieldMap)
	{
	    if (!CollectionUtils.isEmpty(fieldMap))
	    {
	        Iterator it = fieldMap.keySet().iterator();
	        String name = null;

	        while (it.hasNext())
	        {
	            name = (String)it.next();

	            addField(name, fieldMap.get(name));
	        }
	    }
	}

	@Override
	public void addStructure(String name, String fieldName, Object value)
	{
	    if (name != null && !"".equals(name) && fieldName != null && !"".equals(fieldName) && value != null)
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

	@Override
	public void addStructure(String name, Object value)
	{
	    if (name != null && !"".equals(name) && value != null)
	    {
	        Map paramMap = null;

	        if (EasyCollectionFactory.isApproximableMapType(value))
	        {
	            paramMap = (Map)value;
	        }
	        else
	        {
	            paramMap = new BeanPropertyParameterSource(value).asMap();
	        }

	        Iterator it = paramMap.keySet().iterator();
	        String fieldName = null;

	        while (it.hasNext())
	        {
	        	fieldName = (String)it.next();

	            addStructure(name, fieldName, paramMap.get(fieldName));
	        }
	    }
	}

    @Override
	public void addStructure(Map structureMap)
    {
        if (!CollectionUtils.isEmpty(structureMap))
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

	@Override
	public void addTable(String name, List valueList)
	{
		if (name != null && !"".equals(name) && !CollectionUtils.isEmpty(valueList))
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

	    	for (int i=0,size=valueList.size(); i<size; i++)
	    	{
	    		value = valueList.get(i);

		        if (EasyCollectionFactory.isApproximableMapType(value))
		        {
		        	paramMap = (Map)value;
		        }
		        else
		        {
		        	paramMap = new BeanPropertyParameterSource(value).asMap();
		        }

		        index = Integer.toString(i);

		        paramSource.addValue(index, new ParameterValue(index, paramMap));
	    	}
		}
	}

	@Override
	public void addTable(Map tableMap)
	{
	    if (!CollectionUtils.isEmpty(tableMap))
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
}
