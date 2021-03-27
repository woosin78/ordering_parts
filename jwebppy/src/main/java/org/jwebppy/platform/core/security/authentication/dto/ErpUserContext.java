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

	private String corpCode;
	private String username;
	private String custCode;
	private String custName;
	private String custGrp5;
	private String salesOrg;
	private String distChl;
	private String division;
	private String custType;

	public String getCorpName()
	{
		if (corpCode != null)
		{
			if ("1600".equals(corpCode))
			{
				return "DBKR";
			}
		}

		return null;
	}
}