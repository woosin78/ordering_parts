package org.jwebppy.platform.mgmt.content.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemDto extends GeneralDto implements Serializable
{
	private static final long serialVersionUID = -901129428634147884L;

	private Integer cSeq;
	private Integer pSeq;
	private CItemType type;
	private String name;
	private String description;
	private String component;
	private String entryPoint;
	private String parameter;
	private int sort = -1;
	private String fgVisible = PlatformCommonVo.NO;
	private String fgDelete = PlatformCommonVo.NO;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime fromValid;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime toValid;
	private Integer lSeq;
	private List<Integer> seqs;
	private int depth;
	private int subItemCount;
	private List<CItemDto> subCItems;
	private String name2;

	public int getSort()
	{
		if (sort == -1)
		{
			return 100;
		}

		return sort;
	}

	public String getDisplayFromValid()
	{
		return CmDateFormatUtils.format(fromValid);
	}

	public String getDisplayToValid()
	{
		return CmDateFormatUtils.format(toValid);
	}

	public String getFgVisible()
	{
		return CmStringUtils.defaultString(fgVisible, PlatformCommonVo.NO);
	}

	public String getFgDelete()
	{
		return CmStringUtils.defaultString(fgDelete, PlatformCommonVo.NO);
	}

	public String getUrl()
	{
		String url = null;

		if (CmStringUtils.isNotEmpty(entryPoint))
		{
			url = entryPoint;

			if (CmStringUtils.isNotEmpty(parameter))
			{
				url += "?" + parameter;
			}
		}

		return url;
	}
}
