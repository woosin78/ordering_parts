package org.jwebppy.platform.mgmt.logging.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
public class DataAccessLogSearchDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = -725671527598308026L;

	private String dlSeq;
	private String type;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String command;
	private String query;
	private String fgHasResultLog;
	private String referer;

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
