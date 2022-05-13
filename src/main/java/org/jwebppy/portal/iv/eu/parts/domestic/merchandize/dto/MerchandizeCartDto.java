package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto;

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
public class MerchandizeCartDto extends EuGeneralDto
{
	private Integer mcSeq;
	private Integer mpSeq;
	private int orderQty;
	
	private Integer orderPrice;	// 상품 개별가격
	private Integer orderTotalPrice;	// 상품 총합가격
	
	private String materialNo;
	private String productName;
	private String productDescription;
	private String updateFlag;	// 장바구니 추가, 수량변경 여부를 판별하는 문자열 (등록:INSERT, 수량변경:UPDATE)

	private String productImagePath;	// 파일 경로
	private String imageUrl;	// 리스트에 보이는 이미지 url
	
	public MerchandizeCartDto() {}
}
