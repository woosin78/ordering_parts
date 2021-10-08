package org.jwebppy.platform.mgmt.content.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CItemObjectMapper extends GeneralObjectMapper<CItemDto, CItemEntity>
{
	public CItemObjectMapper INSTANCE = Mappers.getMapper(CItemObjectMapper.class);
}
