package org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OrderHistoryItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderHistoryItemMapper extends GeneralObjectMapper<OrderHistoryItemDto, OrderHistoryItemEntity>
{
	public OrderHistoryItemMapper INSTANCE = Mappers.getMapper(OrderHistoryItemMapper.class);
}
