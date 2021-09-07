package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.entity.UserGroupEntity;

@NoLogging
public interface UserGroupMapper
{
	public UserGroupEntity findUserGroup(Integer ugSeq);
	public List<UserGroupEntity> findUserGroups(UserGroupDto userGroup);
}
