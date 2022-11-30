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
public class DataAccessLogParameterDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = -5075894914201107933L;

	private Long dlpSeq;
	private String dlSeq;
	private ParameterType type;
	private String name;
	private List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails;
}
