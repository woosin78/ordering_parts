package org.jwebppy.platform.mgmt.conn_resource.dto;

import lombok.Getter;

@Getter
public enum SapConnType
{
	C("Customer Application Server"), G("Group/Server Selection");

	private String type;

	private SapConnType(String type)
	{
		this.type = type;
	}
}
