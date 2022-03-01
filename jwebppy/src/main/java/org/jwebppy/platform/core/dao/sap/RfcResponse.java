package org.jwebppy.platform.core.dao.sap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.jwebppy.platform.core.dao.AbstractDaoResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.springframework.util.CollectionUtils;

public class RfcResponse extends AbstractDaoResponse implements Serializable
{
	private static final long serialVersionUID = -7318442099344682977L;

	private long startTime;
	private long elapsed;
	private String errorMsg;
	private String destination;
	private String dlSeq;

	private final Map<String, Object> resultMap = new HashMap<>();

    public RfcResponse() {}

    public RfcResponse(Map resultMap)
    {
    	if (!CollectionUtils.isEmpty(resultMap))
    	{
    		this.startTime = (resultMap.get("START_TIME") == null) ? 0 : (Long)resultMap.get("START_TIME");
    		this.elapsed = (resultMap.get("ELAPSED") == null) ? 0 : (Long)resultMap.get("ELAPSED");
    		this.errorMsg = (String)resultMap.get("ERROR_MSG");
    		this.destination = (String)resultMap.get("DESTINATION");

    		this.resultMap.putAll(MapUtils.emptyIfNull((Map)resultMap.get("RESULT")));
    	}
    }

    public long getStartTime()
    {
    	return startTime;
    }

    public long getElapsed()
    {
    	return elapsed;
    }

    public String getErrorMsg()
    {
    	return errorMsg;
    }

    public String getDestination()
    {
    	return destination;
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
    	return getObject(name).toString();
    }

    public DataMap getStructure(String name)
    {
    	return new DataMap(resultMap.get(name));
    }

    public DataList getTable(String name)
    {
    	return new DataList((List)resultMap.get(name));
    }

    public Map<String, Object> getResultMap()
    {
    	return this.resultMap;
    }

    public void setDlSeq(String dlSeq)
    {
    	this.dlSeq = dlSeq;
    }

    public String getDlSeq()
    {
    	return dlSeq;
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
