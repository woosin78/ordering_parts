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
public class MerchandizeCartEntity extends EuGeneralEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6382656625504547324L;
	
	private Integer mcSeq;
	private Integer mpSeq;
	private int orderQty;
	
	private Integer orderPrice;
	private Integer orderTotalPrice;
	
	private String materialNo;
	private String productName;
	private String productDescription;	
	
	private String productImagePath;	// 파일 경로
	private String imageUrl;	// 리스트에 보이는 이미지 url

	public MerchandizeCartEntity() {}
}
