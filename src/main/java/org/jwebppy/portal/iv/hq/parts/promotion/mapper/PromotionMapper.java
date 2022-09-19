package org.jwebppy.portal.iv.hq.parts.promotion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionSearchDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionEntity;

@Mapper
public interface PromotionMapper
{
	public List<PromotionEntity> findPageablePromotions(PromotionSearchDto promotionSearch);
	public List<PromotionEntity> findBannerPromotions(PromotionSearchDto promotionSearch);
	public PromotionEntity findPromotion(Integer pSeq);
	public int insert(PromotionEntity promotionEntity);
	public int update(PromotionEntity promotionEntity);
	public int updateFgDelete(PromotionEntity promotionEntity);
}
