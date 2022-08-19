package org.jwebppy.portal.iv.hq.parts.promotion.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionItemDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromotionItemObjectMapper  extends GeneralObjectMapper<PromotionItemDto, PromotionItemEntity>
{
	public PromotionItemObjectMapper INSTANCE = Mappers.getMapper(PromotionItemObjectMapper.class);
}
