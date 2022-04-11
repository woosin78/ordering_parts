package org.jwebppy.platform.mgmt.mail.dto;

public enum ReceiverType
{
	TO("TO"), CC("CC"), BCC("BCC");

	private String type;

	private ReceiverType(String type)
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
