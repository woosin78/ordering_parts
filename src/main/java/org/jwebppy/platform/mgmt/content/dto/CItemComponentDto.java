package org.jwebppy.platform.mgmt.content.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemComponentDto extends GeneralDto
{
	private static final long serialVersionUID = 9058231060616497826L;

	private String className;
	private String methodName;
	private String url;
}
