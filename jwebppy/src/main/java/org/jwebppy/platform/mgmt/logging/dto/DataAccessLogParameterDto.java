package org.jwebppy.platform.mgmt.logging.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogParameterDto
{
	private Long dalpSeq;
	private Long dalSeq;
	private String type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails;
}
