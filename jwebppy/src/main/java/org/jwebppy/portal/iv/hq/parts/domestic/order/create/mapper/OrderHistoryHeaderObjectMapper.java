package org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OrderHistoryHeaderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderHistoryHeaderObjectMapper extends GeneralObjectMapper<OrderHistoryHeaderDto, OrderHistoryHeaderEntity>
{
	public OrderHistoryHeaderObjectMapper INSTANCE = Mappers.getMapper(OrderHistoryHeaderObjectMapper.class);
}
