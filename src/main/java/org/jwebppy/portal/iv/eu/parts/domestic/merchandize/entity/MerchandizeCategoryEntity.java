package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity;

import org.jwebppy.portal.iv.eu.common.entity.EuGeneralEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MerchandizeCategoryEntity extends EuGeneralEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1730310672259814168L;
	
	private Integer mcSeq;
	private String categoryCode;
	private String categoryName;
	private int sort;
	private String fgDisplay;
	private String fgDelete;

	private String categoryLang;	// 카테고리명 사용자 언어로 가져오기 위한 변수
	
	private String sortStrings;	// 유효한 정렬번호들이 한 줄로 들어 있는 문자열	

	public MerchandizeCategoryEntity() {}
}
