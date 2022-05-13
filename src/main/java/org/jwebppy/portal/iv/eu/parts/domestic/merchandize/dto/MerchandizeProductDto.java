package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
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
public class MerchandizeProductDto extends EuGeneralDto implements IPagination
{
	private Integer mpSeq;
	private Integer mcSeq;
	private int sort;
	private String materialNo;
	private String fgDisplay;
	@Builder.Default
	private String fgDelete = EuMerchandizeCommonVo.NO;
	private String categoryCode;

	private String categoryName;
	private String productName;

	// 사용자 변수 Start
	private Integer price;		// 단가
	private int quantity;	// 수량
	private Integer priceSum;	// 합계
	private String productDescription;
	private String currency;	// 통화단위
	// 사용자 변수 End

	private String[] productNames;
	private String[] productDescs;
	private Integer[] productNameLangDetailSeqs;	// 상품명의 다국어 상세 식별자가 담기는 배열
	private Integer[] productDescLangDetailSeqs;	// 상품설명의 다국어 상세 식별자가 담기는 배열
	private Integer[] productLangKindSeqs;			// 다국어 종류의 식별자가 담기는 배열

	private String productImagePath;	// 파일 경로
	private String imageUrl;	// 리스트에 보이는 이미지 url

	public MerchandizeProductDto() {}
}
