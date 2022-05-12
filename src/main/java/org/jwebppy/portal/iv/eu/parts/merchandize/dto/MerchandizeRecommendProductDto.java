package org.jwebppy.portal.iv.eu.parts.merchandize.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MerchandizeRecommendProductDto extends MerchandizeProductDto
{
	private Integer mrpSeq;
//	private Integer mpSeq;
//	private int sort;
//	private String fgDelete;
	
//	private String materialNo;
//	private String productName;
//	private String productDescription;
//	private Integer price;
	
	private String sortStrings;	// 유효한 정렬번호들이 한 줄로 들어 있는 문자열	
	private String[] mrpSeqs;
	
	private Integer sortMin;
	private Integer sortMax;

	private String productImagePath;	// 파일 경로

	public MerchandizeRecommendProductDto() {}
}
