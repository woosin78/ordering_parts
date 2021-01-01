
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
	private Integer lSeq;
	private String basename;
	private String type;
	private String seq;
	private String fgDelete = PlatformCommonVo.NO;
	private List<LangDetailDto> langDetails;
	private List<Integer> lSeqs;

	public String getMessageCode()
	{
		return CmStringUtils.trimToEmpty(basename) + "_" + CmStringUtils.trimToEmpty(type) + "_" + CmStringUtils.leftPad(seq, 4, "0");
	}

	public String getDisplayType()
	{
		if (type != null)
		{
			if ("LB".equals(type))
			{
				return "LABEL";
			}
			else if ("BTN".equals(type))
			{
				return "BUTTON";
			}
			else if ("TXT".equals(type))
			{
				return "TEXT";
			}
			else if ("MSG".equals(type))
			{
				return "MESSAAE";
			}
		}

		return null;
	}
}
