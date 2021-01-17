package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.entity.UserAccountEntity;
import org.jwebppy.platform.mgmt.user.entity.UserContactInfoEntity;
import org.jwebppy.platform.mgmt.user.entity.UserEntity;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper
{
	public int insertUser(UserEntity user);

	public int insertUserAccount(UserAccountEntity userAccount);

	public int insertUserContactInfo(UserContactInfoEntity userContactInfo);

	public int insertUserPasswordChangeHistory(UserPasswordChangeHistoryEntity userPasswordChangeHistory);

	public int updateUser(UserEntity user);

	public int updateUserAccount(UserAccountEntity userAccount);

	public int updateUserContactInfo(UserContactInfoEntity userContactInfo);

	public int updateFgAccountLocked(UserAccountEntity userAccount);

	public int updateFgDelete(UserEntity user);

	public int updateUsernameToDelete(UserEntity user);

	public UserEntity findUser(UserSearchDto userSearch);

	public UserAccountEntity findUserAccount(int seq);

	public UserContactInfoEntity findUserContactInfo(int seq);

	public List<UserEntity> findPageUsers(UserSearchDto userSearch);

	public List<UserEntity> findUsersInCItem(UserSearchDto userSearch);
}
