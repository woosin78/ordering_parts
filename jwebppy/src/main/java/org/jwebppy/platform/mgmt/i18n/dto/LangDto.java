
package org.jwebppy.platform.mgmt.i18n.dto;

import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = 7599951691132553964L;

	private Integer lSeq;
	private String basename;
	private LangType type;
	private String seq;
	private String fgDelete = PlatformCommonVo.NO;
	private List<LangDetailDto> langDetails;
	private List<Integer> lSeqs;

	public String getMessageCode()
	{
		return CmStringUtils.trimToEmpty(basename) + "_" + type.name() + "_" + seq;
	}

	public String getDisplayType()
	{
		if (type != null)
		{
			return type.getType();
		}

		return null;
	}
}
