package org.jwebppy.platform.core.dao.sap;

import org.jwebppy.platform.core.dao.IDaoRequest;

public class SimpleRfcTemplate extends RfcTemplate
{
	public SimpleRfcTemplate()
	{
		super();
	}

	public SimpleRfcTemplate(JCoConnectionResource jCoConnectionResource)
	{
		super(jCoConnectionResource);
	}

	public RfcResponse response(IDaoRequest daoRequest)
	{
		return new RfcResponse(execute(daoRequest));
	}
}
