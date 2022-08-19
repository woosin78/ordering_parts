package org.jwebppy.portal.iv.hq.parts.promotion.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionTargetDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionTargetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromotionTargetObjectMapper  extends GeneralObjectMapper<PromotionTargetDto, PromotionTargetEntity>
{
	public PromotionTargetObjectMapper INSTANCE = Mappers.getMapper(PromotionTargetObjectMapper.class);
}
