package org.jwebppy.platform.mgmt.logging.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -3252178705437302180L;

	private String dlSeq;
	private IfType type;
	private String command;
	private String className;
	private String methodName;
	private String destination;
	private long startTime;
	private long elapsed;//nano sec
	private String requestId;
	private String sessionId;
	private String error;
	private List<DataAccessLogParameterDto> dataAccessLogParameters;
	private Map<String, Object> executionResult;
	private boolean loggingTarget;

	/*
	public String getDisplayType()
	{
		if (CmStringUtils.isEmpty(type))
		{
			return null;
		}

		if ("J".equals(type))
		{
			return "JDBC";
		}
		else if ("R".equals(type))
		{
			return "RFC";
		}

		return type;
	}
	*/

	public Double getElapsedTime()
	{
		return (elapsed * 0.000000001);
	}

	public String getStartTimeToDate()
	{
		LocalDateTime localDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

		return CmDateFormatUtils.format(localDateTime);
	}

	public String getFinishTimeToDate()
	{
		LocalDateTime localDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime().plusNanos(elapsed);

		return CmDateFormatUtils.format(localDateTime);
	}
}
