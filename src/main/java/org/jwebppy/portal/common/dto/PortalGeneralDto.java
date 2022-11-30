package org.jwebppy.portal.common.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PortalGeneralDto extends GeneralDto
{
	private static final long serialVersionUID = 7568870569871601910L;
	protected String corp;
}

