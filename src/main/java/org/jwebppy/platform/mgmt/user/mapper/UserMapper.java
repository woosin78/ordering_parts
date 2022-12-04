package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.entity.UserAccountEntity;
import org.jwebppy.platform.mgmt.user.entity.UserContactInfoEntity;
import org.jwebppy.platform.mgmt.user.entity.UserEntity;

@NoLogging
@Mapper
public interface UserMapper
{
	public int insertUser(UserEntity user);
	public int insertUserAccount(UserAccountEntity userAccount);
	public int insertUserContactInfo(UserContactInfoEntity userContactInfo);
	public int updateUser(UserEntity user);
	public int updateUserAccount(UserAccountEntity userAccount);
	public int updateUserContactInfo(UserContactInfoEntity userContactInfo);
	public int updateFgAccountLocked(UserAccountEntity userAccount);
	public int updateUsernameToDelete(UserEntity user);
	public int delete(UserEntity user);
	public UserEntity findUser(UserSearchDto userSearch);
	public UserAccountEntity findUserAccount(int useq);
	public UserContactInfoEntity findUserContactInfo(int useq);
	public List<UserEntity> findPageableUsers(UserSearchDto userSearch);
	public List<UserEntity> findUsersInCItem(UserSearchDto userSearch);
}
