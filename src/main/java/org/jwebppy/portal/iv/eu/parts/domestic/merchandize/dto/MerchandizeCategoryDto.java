package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;

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
public class MerchandizeCategoryDto extends EuGeneralDto
{
	private Integer mcSeq;
	private String categoryCode;
	private String categoryName;
	private int sort;
	private String fgDisplay;
	@Builder.Default
	private String fgDelete = EuMerchandizeCommonVo.NO;

	private String categoryLang;	// 카테고리명 사용자 언어로 가져오기 위한 변수

	private String sortStrings;	// 유효한 정렬번호들이 한 줄로 들어 있는 문자열

	private String[] categoryNames;
	private Integer[] categoryNameLangDetailSeqs;
	private Integer[] categoryLangKindSeqs;

	public MerchandizeCategoryDto() {}
}
