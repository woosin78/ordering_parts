package org.jwebppy.platform.mgmt.content.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemLangRlDto extends GeneralDto
{
	private Integer clrSeq;
	private Integer cSeq;
	private Integer lSeq;
	private String basename;
}
