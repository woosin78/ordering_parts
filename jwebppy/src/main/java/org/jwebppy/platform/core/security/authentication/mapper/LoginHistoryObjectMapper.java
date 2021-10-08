package org.jwebppy.platform.core.security.authentication.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.entity.LoginHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoginHistoryObjectMapper extends GeneralObjectMapper<LoginHistoryDto, LoginHistoryEntity>
{
	public LoginHistoryObjectMapper INSTANCE = Mappers.getMapper(LoginHistoryObjectMapper.class);
}
