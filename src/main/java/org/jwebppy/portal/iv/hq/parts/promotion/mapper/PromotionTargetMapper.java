package org.jwebppy.portal.iv.hq.parts.promotion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionTargetDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionTargetEntity;

@Mapper
public interface PromotionTargetMapper 
{
	public List<PromotionTargetEntity> findPromotionTargets(PromotionTargetDto promotionTarget);
	public int insert(PromotionTargetEntity promotionTargetEntity);
	public int updateFgDelete(PromotionTargetEntity promotionTargetEntity);
}
