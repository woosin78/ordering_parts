package org.jwebppy.portal.iv.eu.parts.merchandize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeProductSearchDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeRecommendProductDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeProductEntity;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeRecommendProductEntity;

@Mapper
public interface EuMerchandizeRecommendProductMapper
{
	public List<MerchandizeRecommendProductEntity> findRecommendProductItemsMain(MerchandizeRecommendProductDto reProductDto);
	public MerchandizeRecommendProductEntity findRecommendProductItemsMainCount(MerchandizeRecommendProductDto reProductDto);
	
	public List<MerchandizeProductEntity> findRecommendProductItems(MerchandizeProductSearchDto ProductSearchDto);
		
	public int insertRecommendProductItem(MerchandizeRecommendProductEntity reProductEntity);
	public int updateRecommendProductItem(MerchandizeRecommendProductEntity reProductEntity);
	public MerchandizeRecommendProductEntity checkRecommendProductItem(MerchandizeRecommendProductDto reProductDto);
}
