package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupSearchDto extends GeneralDto
{
	private static final long serialVersionUID = 4787710949029623519L;

	private Integer ugSeq;
	private String name;
}
