package org.jwebppy.platform.mgmt.logging.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataAccessLogObjectMapper extends GeneralObjectMapper<DataAccessLogDto, DataAccessLogEntity>
{
	public DataAccessLogObjectMapper INSTANCE = Mappers.getMapper(DataAccessLogObjectMapper.class);
}
