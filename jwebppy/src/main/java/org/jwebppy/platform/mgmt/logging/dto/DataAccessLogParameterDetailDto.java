package org.jwebppy.platform.mgmt.logging.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogParameterDetailDto extends GeneralDto
{
	private static final long serialVersionUID = -1934434706078161981L;

	private Long dlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
