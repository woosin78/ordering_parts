package org.jwebppy.portal.scm.completes;

import org.jwebppy.platform.core.dao.support.ErpDataMap;

public class CompletesErpDataMap extends ErpDataMap
{
	private static final long serialVersionUID = -5785321232737011635L;

	public CompletesErpDataMap()
	{
		super();
	}

	public CompletesErpDataMap(Object obj)
	{
		super(obj);
	}

	public String getSalesDistrict()
	{
		return getString("BZIRK");
	}
}
