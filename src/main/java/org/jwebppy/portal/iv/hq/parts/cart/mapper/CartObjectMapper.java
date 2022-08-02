package org.jwebppy.portal.iv.hq.parts.cart.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.hq.parts.cart.dto.CartDto;
import org.jwebppy.portal.iv.hq.parts.cart.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartObjectMapper extends GeneralObjectMapper<CartDto, CartEntity>
{
	public CartObjectMapper INSTANCE = Mappers.getMapper(CartObjectMapper.class);
}
