package org.jwebppy.platform.mgmt.logging.dto;

import java.util.List;

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
public class DataAccessResultLogParameterDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = -5075894914201107933L;

	private Long drlpSeq;
	private Long drlSeq;
	private ParameterType type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessResultLogParameterDetailDto> dataAccessResultLogParameterDetails;
}
