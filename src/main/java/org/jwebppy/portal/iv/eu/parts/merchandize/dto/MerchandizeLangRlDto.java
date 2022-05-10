package org.jwebppy.portal.iv.eu.parts.merchandize.dto;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
public class MerchandizeLangRlDto extends EuGeneralDto
{
	private Integer mlrSeq;
	private Integer tSeq;
	private Integer ldSeq;
	private String type;
	
	private Integer lSeq;	
	private Integer lkSeq;
	private String text;
	private String code;
	private String name;
	
	public MerchandizeLangRlDto() {}
}
