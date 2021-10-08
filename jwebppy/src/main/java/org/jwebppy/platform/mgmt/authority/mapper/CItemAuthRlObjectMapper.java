package org.jwebppy.platform.mgmt.authority.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.authority.dto.CItemAuthRlDto;
import org.jwebppy.platform.mgmt.authority.entity.CItemAuthRlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CItemAuthRlObjectMapper extends GeneralObjectMapper<CItemAuthRlDto, CItemAuthRlEntity>
{
	public CItemAuthRlObjectMapper INSTANCE = Mappers.getMapper(CItemAuthRlObjectMapper.class);
}
