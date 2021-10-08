package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPasswordChangeHistoryObjectMapper extends GeneralObjectMapper<UserPasswordChangeHistoryDto, UserPasswordChangeHistoryEntity>
{
	public UserPasswordChangeHistoryObjectMapper INSTANCE = Mappers.getMapper(UserPasswordChangeHistoryObjectMapper.class);
}
