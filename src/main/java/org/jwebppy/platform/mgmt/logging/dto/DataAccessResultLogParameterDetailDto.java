package org.jwebppy.platform.mgmt.logging.dto;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString@SuperBuilder
@NoArgsConstructor
public class DataAccessResultLogParameterDetailDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = -2870889852993063335L;

	private Long drlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
