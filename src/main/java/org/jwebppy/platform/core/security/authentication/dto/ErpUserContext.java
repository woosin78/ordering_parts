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

	private String corp;
	private String username;
	private String custCode;
	private String custName;
	private String salesOrg;
	private String distChl;
	private String division;
	private String plant;
	private String workCenter;
	private String custGrp1;
	private String custGrp2;
	private String custGrp3;
	private String custGrp4;
	private String custGrp5;
	private String customerType;

	public String getCorpName()
	{
		if (corp != null)
		{
			if ("7800".equals(corp))
			{
				return "DIVEU";
			}
			else if ("7200".equals(corp))
			{
				return "DIVUK";
			}
		}

		return null;
	}
}
