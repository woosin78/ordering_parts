package org.jwebppy.platform.core.dao.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.core.CollectionFactory;

public class EasyCollectionFactory
{
	private static final Set<Class<?>> approximableListTypes = new HashSet<Class<?>>(3);

	static
	{
		approximableListTypes.add(List.class);
		approximableListTypes.add(ArrayList.class);
		approximableListTypes.add(LinkedList.class);
	}

	public static boolean isApproximableListType(Class<? extends Object> clazz)
	{
		return ((clazz != null) && (approximableListTypes.contains(clazz)));
	}

	public static boolean isApproximableListType(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj instanceof List || EasyCollectionFactory.isApproximableListType(obj.getClass()))
		{
			return true;
		}

		return false;
	}

	public static boolean isApproximableMapType(Class<?> clazz)
	{
		if (clazz == null)
		{
			return false;
		}

		if (!CollectionFactory.isApproximableMapType(clazz))
		{
			return (BeanUtils.instantiateClass(clazz) instanceof Map);
		}

		return true;
	}

	public static boolean isApproximableMapType(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj instanceof Map || CollectionFactory.isApproximableMapType(obj.getClass()))
		{
			return true;
		}

		return false;
	}

	public static boolean isApproximableCollectionType(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj instanceof Collection || CollectionFactory.isApproximableCollectionType(obj.getClass()))
		{
			return true;
		}

		return false;
	}
}
