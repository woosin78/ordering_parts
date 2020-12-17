package org.jwebppy.platform.security.authentication.dto;

import java.time.LocalDateTime;

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
public class LoginHistorySearchDto extends GeneralDto implements IPagination
{
	private Integer uSeq;
	private String username;
	private String fgResult;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime fromDate;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime toDate;
	private String query;
}
