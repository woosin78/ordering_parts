package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -532421241477305133L;

	private Integer ugSeq;
	private String name;
	private String description;
	private SapConnResourceDto sapConnResource;

	public UserGroupDto() {}

	public UserGroupDto(Integer ugSeq)
	{
		this.ugSeq = ugSeq;
	}

	public SapConnResourceDto getSapConnResource()
	{
		return (sapConnResource == null) ? new SapConnResourceDto() : sapConnResource;
	}
}
