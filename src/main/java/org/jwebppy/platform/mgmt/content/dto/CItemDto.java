package org.jwebppy.platform.mgmt.content.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Builder;
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
public class CItemDto extends MgmtGeneralDto implements IPagination
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
	@Builder.Default
	private int sort = 100;
	@Builder.Default
	private String fgVisible = MgmtCommonVo.NO;
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
