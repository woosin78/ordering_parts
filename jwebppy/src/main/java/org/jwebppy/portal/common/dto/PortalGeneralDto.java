package org.jwebppy.portal.common.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PortalGeneralDto extends GeneralDto
{
	private static final long serialVersionUID = 7568870569871601910L;
	private String corp;
}
