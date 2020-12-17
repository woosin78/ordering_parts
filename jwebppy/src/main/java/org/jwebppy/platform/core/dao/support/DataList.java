package org.jwebppy.platform.core.dao.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.util.CollectionUtils;

public class DataList implements IDataList, Serializable
{
	private static final long serialVersionUID = -8699636927111870541L;

	private final List list = new ArrayList();

	public DataList() {}

	public DataList(List list)
	{
		if (!CollectionUtils.isEmpty(list))
		{
			this.list.addAll(list);
		}
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return list.contains(o);
	}

	@Override
	public Iterator iterator()
	{
		return list.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return list.toArray();
	}

	@Override
	public Object[] toArray(Object[] a)
	{
		return list.toArray();
	}

	@Override
	public boolean add(Object o)
	{
		return list.add(o);
	}

	@Override
	public boolean remove(Object o)
	{
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection c)
	{
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection c)
	{
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection c)
	{
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection c)
	{
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection c)
	{
		return list.retainAll(c);
	}

	@Override
	public void clear()
	{
		list.clear();
	}

	@Override
	public Object get(int index)
	{
		Object obj = list.get(index);

		if (EasyCollectionFactory.isApproximableMapType(obj))
		{
			return toDataMap(list.get(index));
		}

	    return obj;
	}

	@Override
	public Object set(int index, Object element)
	{
		return list.set(index, element);
	}

	@Override
	public void add(int index, Object element)
	{
		list.add(index, element);
	}

	@Override
	public Object remove(int index)
	{
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator listIterator()
	{
		return list.listIterator();
	}

	@Override
	public ListIterator listIterator(int index)
	{
		return list.listIterator(index);
	}

	@Override
	public List subList(int fromIndex, int toIndex)
	{
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public String toString()
	{
		return list.toString();
	}

	private DataMap toDataMap(Object value)
	{
		return new DataMap((Map)value);
	}
}
