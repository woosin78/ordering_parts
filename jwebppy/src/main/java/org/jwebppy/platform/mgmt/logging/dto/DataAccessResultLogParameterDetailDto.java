package org.jwebppy.platform.mgmt.logging.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessResultLogParameterDetailDto extends GeneralDto
{
	private static final long serialVersionUID = -2870889852993063335L;

	private Long drlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
