package org.jwebppy.platform.core.dao;

import java.util.Collection;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.EasyCollectionFactory;
import org.jwebppy.platform.core.dao.support.IDataList;

public abstract class AbstractDaoResponse implements IDaoResponse
{
	private IDataList dataList = new DataList();
	private int fetchSize = 0;

	public AbstractDaoResponse() {}

	public AbstractDaoResponse(Object obj)
	{
    	if (obj != null)
    	{
            if (EasyCollectionFactory.isApproximableCollectionType(obj))
            {
            	dataList.addAll((Collection)obj);
            }
            else if (EasyCollectionFactory.isApproximableMapType(obj))
            {
            	dataList.add(new DataMap((Map)obj));
            }
            else
            {
            	dataList.add(obj);
            }

            setFetchSize(dataList.size());
    	}
	}

	@Override
	public IDataList getList()
	{
		return dataList;
	}

	@Override
	public Object getFirstRow()
	{
		return dataList.get(0);
	}

	@Override
	public Object getRow(int index)
	{
		return (getFetchSize() > index) ? dataList.get(index) : null;
	}

	@Override
	public void setFetchSize(int fetchSize)
	{
		this.fetchSize = fetchSize;
	}

	@Override
	public int getFetchSize()
	{
		return fetchSize;
	}
}
