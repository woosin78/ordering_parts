package org.jwebppy.platform.mgmt.logging.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogParameterDetailDto
{
	private long dalpSeq;
	private int lineNo;
	private String name;
	private String value;
}
