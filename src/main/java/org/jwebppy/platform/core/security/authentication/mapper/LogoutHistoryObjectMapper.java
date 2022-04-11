package org.jwebppy.platform.core.security.authentication.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.core.security.authentication.dto.LogoutHistoryDto;
import org.jwebppy.platform.core.security.authentication.entity.LogoutHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogoutHistoryObjectMapper extends GeneralObjectMapper<LogoutHistoryDto, LogoutHistoryEntity>
{
	public LogoutHistoryObjectMapper INSTANCE = Mappers.getMapper(LogoutHistoryObjectMapper.class);
}
