package org.jwebppy.portal.iv.hq.parts.promotion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionItemDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionItemEntity;

@Mapper
public interface PromotionItemMapper 
{
	public List<PromotionItemEntity> findPromotionItems(PromotionItemDto promotionItem);
	public int insert(PromotionItemEntity promotionItemEntity);
	public int updateFgDelete(PromotionItemEntity promotionItemEntity);
}
