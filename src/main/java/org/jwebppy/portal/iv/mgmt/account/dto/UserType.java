package org.jwebppy.portal.iv.mgmt.account.dto;

public enum UserType
{
	D("Dealer"), I("Internal User");

	private String type;

	private UserType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
