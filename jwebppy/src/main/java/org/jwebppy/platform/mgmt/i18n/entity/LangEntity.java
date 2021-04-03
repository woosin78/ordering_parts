package org.jwebppy.platform.mgmt.i18n.entity;

import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.i18n.dto.LangType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangEntity extends GeneralEntity implements IPagination
{
	private static final long serialVersionUID = -4634138279238831478L;
	private Integer lSeq;
	private String basename;
	private LangType type;
	private String seq;
	private String fgDelete = PlatformCommonVo.NO;
	private List<LangDetailEntity> langDetails;

	public String getSeq2()
	{
		String prefix = CmStringUtils.trimToEmpty(basename) + "_" + type.name() + "_";

		if (CmStringUtils.startsWithIgnoreCase(seq, prefix))
		{
			return CmStringUtils.removeStart(seq, prefix);
		}

		return seq;
	}
}
