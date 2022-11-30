package org.jwebppy.platform.core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;

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
public abstract class GeneralDto implements Serializable
{
	private static final long serialVersionUID = -2141681838401701675L;

	protected String fgDelete = MgmtCommonVo.NO;
	protected String regUsername;
	protected LocalDateTime regDate;
	protected String modUsername;
	protected LocalDateTime modDate;

	protected int rnum;
	protected int pageNumber = 1;
	protected int rowPerPage = PlatformCommonVo.DEFAULT_ROW_PER_PAGE;
	protected int totalCount = 0;

	public String getFgDelete()
	{
		return CmStringUtils.defaultIfEmpty(fgDelete, PlatformCommonVo.NO);
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

	public String getDisplayRegDate2()
	{
		return CmDateFormatUtils.format(regDate, null, CmDateFormatUtils.getDateFormat());
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
