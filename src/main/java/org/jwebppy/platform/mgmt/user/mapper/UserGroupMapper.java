package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.dto.UserGroupSearchDto;
import org.jwebppy.platform.mgmt.user.entity.UserGroupEntity;

@NoLogging
@Mapper
public interface UserGroupMapper
{
	public int insert(UserGroupEntity userGroup);
	public int update(UserGroupEntity userGroup);
	public int updateFgDelete(UserGroupEntity userGroup);
	public UserGroupEntity findUserGroup(Integer ugSeq);
	public UserGroupEntity findUserGroupByName(String name);
	public List<UserGroupEntity> findUserGroups(UserGroupSearchDto userGroupSearch);
	public List<UserGroupEntity> findPageUserGroups(UserGroupSearchDto userGroupSearch);
}
