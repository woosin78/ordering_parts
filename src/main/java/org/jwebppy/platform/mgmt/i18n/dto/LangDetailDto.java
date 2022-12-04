package org.jwebppy.platform.mgmt.i18n.dto;

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
public class LangDetailDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 2760099488348400393L;

	private Integer ldSeq;
	private Integer lseq;
	private Integer lkSeq;
	private String text;
	private LangKindDto langKind;
}
