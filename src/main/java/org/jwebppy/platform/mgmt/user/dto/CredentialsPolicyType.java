package org.jwebppy.platform.mgmt.user.dto;

public enum CredentialsPolicyType
{
	U("USERNAME"), P("PASSWORD");

	private String type;

	private CredentialsPolicyType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
