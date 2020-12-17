package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.jwebppy.config.RedisConfig;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.entity.UserAccountEntity;
import org.jwebppy.platform.mgmt.user.entity.UserContactInfoEntity;
import org.jwebppy.platform.mgmt.user.entity.UserEntity;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper
{
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int insertUser(UserEntity userEntity);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int insertUserAccount(UserAccountEntity userAccount);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int insertUserContactInfo(UserContactInfoEntity userContactInfo);
	public int insertUserPasswordChangeHistory(UserPasswordChangeHistoryEntity userPasswordChangeHistory);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int updateUser(UserEntity user);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int updateUserAccount(UserAccountEntity userAccount);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int updateUserContactInfo(UserContactInfoEntity userContactInfo);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int updateFgAccountLocked(UserAccountEntity userAccount);
	@CacheEvict(value = RedisConfig.USER, allEntries = true)
	public int updateFgDelete(UserEntity userEntity);
	@Cacheable(value = RedisConfig.USER, key = "#userSearch", unless="#result == null")
	public UserEntity findUser(UserSearchDto userSearch);
	public UserAccountEntity findUserAccount(int seq);
	public UserContactInfoEntity findUserContactInfo(int seq);
	public List<UserEntity> findUsers(UserSearchDto userSearch);
}
