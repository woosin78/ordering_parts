package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupDto extends GeneralDto
{
	private static final long serialVersionUID = -532421241477305133L;

	private Integer ugSeq;
	private String name;
	private String description;
	private Integer scrSeq;

	public UserGroupDto() {}

	public UserGroupDto(Integer ugSeq)
	{
		this.ugSeq = ugSeq;
	}
}
