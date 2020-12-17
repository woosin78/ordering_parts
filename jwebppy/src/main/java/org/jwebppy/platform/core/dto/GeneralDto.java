package org.jwebppy.platform.core.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeneralDto
{
	protected String regUsername;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	protected LocalDateTime regDate;
	protected String modUsername;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	protected LocalDateTime modDate;

	protected int rnum = 0;
	protected int rowNo = 0;
	protected int pageNumber = 1;
	protected int rowPerPage = 20;
	protected int totalCount = 0;

	public String getDisplayRegDate()
	{
		return CmDateFormatUtils.format(regDate);
	}

	public String getDisplayModDate()
	{
		return CmDateFormatUtils.format(modDate);
	}
}
