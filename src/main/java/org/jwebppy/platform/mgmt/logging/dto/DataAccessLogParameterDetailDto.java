package org.jwebppy.platform.mgmt.logging.dto;

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
public class DataAccessLogParameterDetailDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = -1934434706078161981L;

	private Long dlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
