
package org.jwebppy.platform.mgmt.i18n.dto;

import java.util.List;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class LangDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = 7599951691132553964L;

	private Integer lseq;
	private String basename;
	private LangType type;
	private String seq;
	private List<LangDetailDto> langDetails;
	private List<Integer> lseqs;

	public String getPrefix()
	{
		return CmStringUtils.trimToEmpty(basename) + "_" + type.name() + "_";
	}

	public String getCode()
	{
		return getPrefix() + seq;
	}

	public String getSeq2()
	{
		if (CmStringUtils.startsWithIgnoreCase(seq, getPrefix()))
		{
			return CmStringUtils.removeStart(seq, getPrefix());
		}

		return seq;
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
