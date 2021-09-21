package org.jwebppy.platform.core.dao.sap;

import org.jwebppy.platform.core.dao.AbstractDaoRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RfcRequest extends AbstractDaoRequest
{
	private String connectorName;
	private String functionName;

	public RfcRequest() {}

	public RfcRequest(String functionName)
	{
		this.functionName = functionName;
	}

	public RfcRequest(String connectorName, String functionName)
	{
		this.connectorName = connectorName;
		this.functionName = functionName;
	}
}
