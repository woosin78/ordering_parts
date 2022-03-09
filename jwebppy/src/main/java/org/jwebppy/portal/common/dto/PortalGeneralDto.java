package org.jwebppy.portal.common.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PortalGeneralDto extends GeneralDto
{
	private static final long serialVersionUID = 7568870569871601910L;
	private String corp;
}
