package org.jwebppy.platform.core.dao;

import org.jwebppy.platform.core.dao.support.IDataList;

public interface IDaoResponse
{
	public IDataList getList();
	public Object getFirstRow();
	public Object getRow(int index);
	public void setFetchSize(int fetchSize);
	public int getFetchSize();
}
