package org.jwebppy.platform.mgmt.logging.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
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
public class DataAccessLogDto extends MgmtGeneralDto implements IPagination
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
	private String requestUri;
	private String referer;
	private String sessionId;
	private String error;
	private String timezone;
	private List<DataAccessLogParameterDto> dataAccessLogParameters;
	private Map<String, Object> executionResult;
	private boolean loggingTarget;

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
