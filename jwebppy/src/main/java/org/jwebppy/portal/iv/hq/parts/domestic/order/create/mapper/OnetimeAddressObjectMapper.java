package org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OnetimeAddressDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OnetimeAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OnetimeAddressObjectMapper extends GeneralObjectMapper<OnetimeAddressDto, OnetimeAddressEntity>
{
	public OnetimeAddressObjectMapper INSTANCE = Mappers.getMapper(OnetimeAddressObjectMapper.class);
}
