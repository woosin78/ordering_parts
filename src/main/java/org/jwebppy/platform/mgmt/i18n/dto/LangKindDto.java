package org.jwebppy.platform.mgmt.i18n.dto;

import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangKindDto extends GeneralDto
{
	private static final long serialVersionUID = -8395137382255893L;

	private Integer lkSeq;
	private String basename;
	private String name;
	private String code;
	private int sort;
	private String fgDefault = MgmtCommonVo.NO;

	public LangKindDto() {}

	public LangKindDto(Integer lkSeq)
	{
		this.lkSeq = lkSeq;
	}

	public LangKindDto(String basename)
	{
		this.basename = basename;
	}
}
