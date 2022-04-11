package org.jwebppy.platform.mgmt.logging.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataAccessLogParameterObjectMapper extends GeneralObjectMapper<DataAccessLogParameterDto, DataAccessLogParameterEntity>
{
	public DataAccessLogParameterObjectMapper INSTANCE = Mappers.getMapper(DataAccessLogParameterObjectMapper.class);
}
