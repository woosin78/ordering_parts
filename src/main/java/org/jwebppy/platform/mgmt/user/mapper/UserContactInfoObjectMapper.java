package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.user.dto.UserContactInfoDto;
import org.jwebppy.platform.mgmt.user.entity.UserContactInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserContactInfoObjectMapper extends GeneralObjectMapper<UserContactInfoDto, UserContactInfoEntity>
{
	public UserContactInfoObjectMapper INSTANCE = Mappers.getMapper(UserContactInfoObjectMapper.class);
}
