package org.jwebppy.platform.mgmt.i18n.dto;

import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangDetailDto extends GeneralDto
{
	private static final long serialVersionUID = 2760099488348400393L;

	private Integer ldSeq;
	private Integer lSeq;
	private Integer lkSeq;
	private String text;
	private String fgDelete = MgmtCommonVo.NO;
	private LangKindDto langKind;
}
