package org.jwebppy.platform.core.security.authentication.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ErpUserContext implements Serializable
{
	private static final long serialVersionUID = 3856836169522676329L;

	private String corpNo;
	private String username;
	private String customerNo;
	private String customerName;
	private String customerGrp5;
	private String salesOrg;
	private String distChannel;
	private String division;
	private String customerType;

	public String getCorpName()
	{
		if (corpNo != null)
		{
			if ("7800".equals(corpNo))
			{
				return "DIVEU";
			}
			else if ("7200".equals(corpNo))
			{
				return "DIVUK";
			}
		}

		return null;
	}
}
