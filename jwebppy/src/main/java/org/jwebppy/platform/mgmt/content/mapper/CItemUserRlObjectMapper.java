package org.jwebppy.platform.mgmt.content.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CItemUserRlObjectMapper extends GeneralObjectMapper<CItemUserRlDto, CItemUserRlEntity>
{
	public CItemUserRlObjectMapper INSTANCE = Mappers.getMapper(CItemUserRlObjectMapper.class);
}
