package org.jwebppy.portal.scm.parts;

import org.jwebppy.platform.core.dao.support.ErpDataMap;

public class PartsErpDataMap extends ErpDataMap
{
	private static final long serialVersionUID = 8431381483530973785L;

	public PartsErpDataMap()
	{
		super();
	}

	public PartsErpDataMap(Object obj)
	{
		super(obj);
	}

	public String getCustomerGrp5()
	{
		return getString("KVGR5");
	}

	public String getCustomerType()
	{
		return getString("TYPE");
	}
}
