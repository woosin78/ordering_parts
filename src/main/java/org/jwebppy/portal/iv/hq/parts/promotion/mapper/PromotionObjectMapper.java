package org.jwebppy.portal.iv.hq.parts.promotion.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromotionObjectMapper  extends GeneralObjectMapper<PromotionDto, PromotionEntity>
{
	public PromotionObjectMapper INSTANCE = Mappers.getMapper(PromotionObjectMapper.class);
}
