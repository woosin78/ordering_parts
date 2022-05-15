package org.jwebppy.portal.iv.hq.parts.export.order.create.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderHistoryItemDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.entity.ExOrderHistoryItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExOrderHistoryItemObjectMapper extends GeneralObjectMapper<ExOrderHistoryItemDto, ExOrderHistoryItemEntity>
{
	public ExOrderHistoryItemObjectMapper INSTANCE = Mappers.getMapper(ExOrderHistoryItemObjectMapper.class);
}
