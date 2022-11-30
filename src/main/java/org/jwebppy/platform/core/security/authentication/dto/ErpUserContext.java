package org.jwebppy.platform.core.security.authentication.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
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
	private String shippingCondition;
	private String customerType;

	public String getCorpName()
	{
		if (corp != null)
		{
			if ("8706".equals(corp))
			{
				return "DIEU";
			}
			else if ("8716".equals(corp))
			{
				return "DIUK";
			}
		}

		return null;
	}
}
