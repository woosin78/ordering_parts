package org.jwebppy.platform.core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeneralDto implements Serializable
{
	private static final long serialVersionUID = -2141681838401701675L;

	protected String fgDelete;
	protected String regUsername;
	protected LocalDateTime regDate;
	protected String modUsername;
	protected LocalDateTime modDate;

	protected int no = 0;
	protected int rnum = 0;
	protected int pageNumber = 1;
	protected int rowPerPage = PlatformCommonVo.DEFAULT_ROW_PER_PAGE;
	protected int totalCount = 0;

	public String getFgDelete()
	{
		return CmStringUtils.defaultString(fgDelete, PlatformCommonVo.NO);
	}

	public int getNo()
	{
		return totalCount - rnum + 1;
	}

	public String getDateTimeFormat()
	{
		return CmStringUtils.defaultIfEmpty(UserAuthenticationUtils.getUserDetails().getDateTimeFormat1(), PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT);
	}

	public String getDisplayRegDate()
	{
		return CmDateFormatUtils.format(regDate);
	}

	public String getDisplayModDate()
	{
		return CmDateFormatUtils.format(modDate);
	}

	public int getStartRow()
	{
		return (pageNumber - 1) * rowPerPage;
	}
}
