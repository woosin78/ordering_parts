package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupEntity extends GeneralDto
{
	private static final long serialVersionUID = -2864103624808776308L;

	private Integer ugSeq;
	private String name;
	private String description;
	private String fgDelete;
}
