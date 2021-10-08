package org.jwebppy.platform.mgmt.logging.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataAccessResultLogObjectMapper extends GeneralObjectMapper<DataAccessResultLogDto, DataAccessResultLogEntity>
{
	public DataAccessResultLogObjectMapper INSTANCE = Mappers.getMapper(DataAccessResultLogObjectMapper.class);
}
