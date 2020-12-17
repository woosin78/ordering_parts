package org.jwebppy.platform.core.dao;

import java.util.Map;

public interface IDaoTemplate
{
	public Map<String, Object> execute(IDaoRequest daoRequest);
}
