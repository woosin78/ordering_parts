package org.jwebppy.platform.mgmt.logging.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogParameterDto extends GeneralDto
{
	private static final long serialVersionUID = -5075894914201107933L;

	private Long dlpSeq;
	private String dlSeq;
	private ParameterType type;
	private String name;
	private List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails;
}