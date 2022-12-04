package org.jwebppy.platform.mgmt.i18n.entity;

import java.util.List;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.jwebppy.platform.mgmt.i18n.dto.LangType;

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
public class LangEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = -4634138279238831478L;
	private Integer lseq;
	private String basename;
	private LangType type;
	private String seq;
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
