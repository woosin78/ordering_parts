package org.jwebppy.platform.core.dao;

import java.util.Map;

public interface IDaoRequest
{
	public void addParameter(String name, Object value);
	//public void addParameter(ParameterValue value);
	public Object getParameter(String name);
	//public void addBatchParameter(Object value);
	//public void addOutputParameter(String name, int type, Class clazz);
	public void addOutputParameter(OutputParameter param);
	//public String[] getOutputParameterNames();
	public Map getOutputParameterMap();
	public void setRowMapperType(Class rowMapperType);
	public Class getRowMapperType();
}
