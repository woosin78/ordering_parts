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
public class MerchandizeProductEntity extends EuGeneralEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9015124758824296143L;
	
	private Integer mpSeq;
	private Integer mcSeq;	
	private int sort;
	private String materialNo;
	private String fgDisplay;
	private String fgDelete;

	// 사용자 변수 Start
	private Integer price;		// 단가
	private int quantity;	// 수량
	private Integer priceSum;	// 합계
	private String productDescription;
	private String currency;	// 통화단위
	// 사용자 변수 End
	
	private String categoryCode;
	private String categoryName;
	private String productName;
	
	private String productImagePath;	// 파일 경로
	private String imageUrl;	// 리스트에 보이는 이미지 url
	
	public MerchandizeProductEntity() {}
}
