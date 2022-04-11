package org.jwebppy.platform.mgmt.conn_resource.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.entity.SapConnResourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SapConnResourceObjectMapper extends GeneralObjectMapper<SapConnResourceDto, SapConnResourceEntity>
{
	public SapConnResourceObjectMapper INSTANCE = Mappers.getMapper(SapConnResourceObjectMapper.class);
}
