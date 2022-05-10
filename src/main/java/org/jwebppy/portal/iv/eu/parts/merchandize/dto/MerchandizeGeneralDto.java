package org.jwebppy.portal.iv.eu.parts.merchandize.dto;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MerchandizeGeneralDto extends EuGeneralDto
{
	private Integer tSeq;
	private int sort;
	
	// 정렬순서 조정용 변수 Start
	private int minNum;
	private int maxNum;
	private int targetNum;
	private String plusOrMinusStr;
	private String tableNm;
	private String tSeqStrArray;
	private String[] tSeqs;
	// 정렬순서 조정용 변수 End	
	
	public MerchandizeGeneralDto() {}
}
