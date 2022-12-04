package org.jwebppy.platform.mgmt.content.dto;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class CItemLangRlDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 8851437491168803824L;

	private Integer clrSeq;
	private Integer cseq;
	private Integer lseq;
	private String basename;
}
