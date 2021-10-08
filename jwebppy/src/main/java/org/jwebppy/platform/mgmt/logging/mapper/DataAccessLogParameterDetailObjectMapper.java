package org.jwebppy.platform.mgmt.logging.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataAccessLogParameterDetailObjectMapper extends GeneralObjectMapper<DataAccessLogParameterDetailDto, DataAccessLogParameterDetailEntity>
{
	public DataAccessLogParameterDetailObjectMapper INSTANCE = Mappers.getMapper(DataAccessLogParameterDetailObjectMapper.class);
}
