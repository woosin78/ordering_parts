package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.entity.UserGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupObjectMapper extends GeneralObjectMapper<UserGroupDto, UserGroupEntity>
{
	public UserGroupObjectMapper INSTANCE = Mappers.getMapper(UserGroupObjectMapper.class);
}
