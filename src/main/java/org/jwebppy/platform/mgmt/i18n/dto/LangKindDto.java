package org.jwebppy.platform.mgmt.i18n.dto;

import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Builder;
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
public class LangKindDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = -8395137382255893L;

	private Integer lkSeq;
	private String basename;
	private String name;
	private String code;
	private int sort;
	@Builder.Default
	private String fgDefault = MgmtCommonVo.NO;

	public LangKindDto(Integer lkSeq)
	{
		this.lkSeq = lkSeq;
	}

	public LangKindDto(String basename)
	{
		this.basename = basename;
	}
}
