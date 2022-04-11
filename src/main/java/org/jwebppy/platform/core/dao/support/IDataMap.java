package org.jwebppy.platform.core.dao.support;

import java.util.Iterator;
import java.util.Map;

public interface IDataMap extends Map, IDataHandle
{
    public Iterator iteratorByKeySet();
    public Iterator iteratorByEntrySet();
    public IDataMap getMap(Object key);
    public IDataList getList(Object key);
}
