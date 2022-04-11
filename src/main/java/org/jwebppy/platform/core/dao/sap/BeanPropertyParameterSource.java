package org.jwebppy.platform.core.dao.sap;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.AbstractParameterSource;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;

public class BeanPropertyParameterSource extends AbstractParameterSource
{
	private final BeanWrapper beanWrapper;
	private String[] propertyNames;

	public BeanPropertyParameterSource(Object obj)
	{
		this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
	}

	@Override
	public boolean hasValue(String paramName)
	{
		return this.beanWrapper.isReadableProperty(paramName);
	}

	@Override
	public Object getValue(String paramName) throws IllegalArgumentException
	{
		try
		{
			return this.beanWrapper.getPropertyValue(paramName);
		}
		catch (NotReadablePropertyException e)
		{
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public String[] getReadablePropertyNames()
	{
		if (this.propertyNames == null)
		{
			List nameList = new ArrayList();
			PropertyDescriptor[] props = this.beanWrapper.getPropertyDescriptors();

			for (int i=0, len=props.length; i<len; ++i)
			{
				if (this.beanWrapper.isReadableProperty(props[i].getName()))
				{
					nameList.add(props[i].getName());
				}
			}

			this.propertyNames = (String[])nameList.toArray(new String[nameList.size()]);
		}

		return this.propertyNames;
	}

	public Map<String, Object> asMap()
	{
		Map<String, Object> propertyMap = new LinkedHashMap<>();

    	String[] names = getReadablePropertyNames();

        if (names != null)
        {
            for (int i=0,len=names.length; i<len; i++)
            {
                if ("class".equals(names[i]))
                {
                    continue;
                }

                propertyMap.put(names[i], getValue(names[i]));
            }
        }

        return propertyMap;
	}
}
