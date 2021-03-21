package org.jwebppy.platform.mgmt.logging.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessResultLogParameterDto extends GeneralDto
{
	private static final long serialVersionUID = -5075894914201107933L;

	private Long drlpSeq;
	private Long drlSeq;
	private ParameterType type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessResultLogParameterDetailDto> dataAccessResultLogParameterDetails;
}
