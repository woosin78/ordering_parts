package org.jwebppy.platform.mgmt.content.dto;

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
public class CItemComponentDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 9058231060616497826L;

	private String className;
	private String methodName;
	private String url;
}
