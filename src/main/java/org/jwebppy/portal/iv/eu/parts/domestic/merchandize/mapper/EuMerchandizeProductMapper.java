package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductSearchDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeProductEntity;

@Mapper
public interface EuMerchandizeProductMapper
{
	public MerchandizeProductEntity findProductItem(MerchandizeProductDto productDto);
	public List<MerchandizeProductEntity> findProductItems(MerchandizeProductSearchDto productSearchDto);
	public int insertProductItem(MerchandizeProductEntity productEntity);
	public int updateProductItem(MerchandizeProductEntity productEntity);	
	
	public MerchandizeProductEntity verifyProductItem(MerchandizeProductDto productDto);	// 상품코드 중복체크
//	public MerchandizeProductEntity findProductPk(MerchandizeProductDto productDto);	// 상품 테이블 PK취득
	
	public List<MerchandizeProductEntity> findProductItemsUser(MerchandizeProductSearchDto productSearchDto);
}
