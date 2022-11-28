package org.jwebppy.platform.mgmt.content.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -7199501796641936242L;

	private Integer cSeq;
	private Integer pSeq;
	private CItemType type;
	private String name;
	private String name2;
	private String description;
	private String component;
	private String entryPoint;
	private String parameter;
	private int sort = 100;
	private String fgVisible = MgmtCommonVo.NO;
	private String fgDelete = MgmtCommonVo.NO;
	private LocalDateTime fromValid;
	private LocalDateTime toValid;
	private String launchType;
	private String scrWidth;
	private String scrHeight;
	private Integer lSeq;
	private List<Integer> seqs;
	private int depth;
	private int userCount;
	private int subItemCount;
	private List<CItemDto> subCItems;

	public String getDisplayFromValid()
	{
		return CmDateFormatUtils.format(fromValid);
	}

	public String getDisplayToValid()
	{
		return CmDateFormatUtils.format(toValid);
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
