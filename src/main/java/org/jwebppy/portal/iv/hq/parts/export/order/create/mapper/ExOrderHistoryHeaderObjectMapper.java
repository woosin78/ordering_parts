package org.jwebppy.portal.iv.hq.parts.export.order.create.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.entity.ExOrderHistoryHeaderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExOrderHistoryHeaderObjectMapper extends GeneralObjectMapper<ExOrderHistoryHeaderDto, ExOrderHistoryHeaderEntity>
{
	public ExOrderHistoryHeaderObjectMapper INSTANCE = Mappers.getMapper(ExOrderHistoryHeaderObjectMapper.class);
}
