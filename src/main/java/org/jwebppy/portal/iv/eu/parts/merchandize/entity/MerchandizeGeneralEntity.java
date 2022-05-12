package org.jwebppy.portal.iv.eu.parts.merchandize.entity;

import org.jwebppy.portal.iv.eu.common.entity.EuGeneralEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MerchandizeGeneralEntity extends EuGeneralEntity
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3007680798451439124L;

	private Integer tSeq;
	private int sort;
	
	// 정렬순서 조정용 변수 Start
	private int minNum;
	private int maxNum;
	private int targetNum;
	private String plusOrMinusStr;
	private String tableNm;
	private String tSeqStrArray;
	// 정렬순서 조정용 변수 End

	public MerchandizeGeneralEntity() {}
}
