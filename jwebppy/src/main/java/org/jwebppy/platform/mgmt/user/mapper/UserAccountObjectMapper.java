package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.entity.UserAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountObjectMapper extends GeneralObjectMapper<UserAccountDto, UserAccountEntity>
{
	public UserAccountObjectMapper INSTANCE = Mappers.getMapper(UserAccountObjectMapper.class);
}
