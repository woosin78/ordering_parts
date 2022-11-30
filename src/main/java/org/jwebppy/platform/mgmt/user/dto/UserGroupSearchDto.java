package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class UserGroupSearchDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 4787710949029623519L;

	private Integer ugSeq;
	private String name;
	private String query;
}
