package org.jwebppy.platform.mgmt.logging.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogSearchDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -725671527598308026L;

	private String dlSeq;
	private String type;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS)
	private LocalDateTime fromDate;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS)
	private LocalDateTime toDate;
	private String command;
	private String query;
	private String fgHasResultLog;

	public long getFromDateToMs()
	{
		if (fromDate != null)
		{
		    long milliseconds = fromDate
		            .atZone(ZoneId.systemDefault())
		            .toInstant()
		            .toEpochMilli();

		    return milliseconds;
		}

		return 0;
	}

	public long getToDateToMs()
	{
		if (toDate != null)
		{
		    long milliseconds = toDate
		            .atZone(ZoneId.systemDefault())
		            .toInstant()
		            .toEpochMilli();

		    return milliseconds;
		}

		return 0;
	}
}
