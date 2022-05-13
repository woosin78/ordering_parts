package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MerchandizeRecommendProductEntity extends MerchandizeProductEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6662645581766608799L;

	private Integer mrpSeq;
	
	private String sortStrings;	// 유효한 정렬번호들이 한 줄로 들어 있는 문자열

	private Integer sortMin;
	private Integer sortMax;
	
	private String productImagePath;	// 파일 경로

	public MerchandizeRecommendProductEntity() {}
}
