package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserObjectMapper extends GeneralObjectMapper<UserDto, UserEntity>
{
	public UserObjectMapper INSTANCE = Mappers.getMapper(UserObjectMapper.class);
}
