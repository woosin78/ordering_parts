package org.jwebppy.platform.mgmt.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserContactInfoDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.entity.UserAccountEntity;
import org.jwebppy.platform.mgmt.user.entity.UserContactInfoEntity;
import org.jwebppy.platform.mgmt.user.entity.UserEntity;
import org.jwebppy.platform.mgmt.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends GeneralService
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserPasswordChangeHistoryService userPasswordChangeHistoryService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public int createUser(UserDto user)
	{
		UserEntity userEntity = CmModelMapperUtils.map(user, UserEntity.class);

		userMapper.insertUser(userEntity);

		return userEntity.getUSeq();
	}

	public int createUserAccount(UserAccountDto userAccount)
	{
		userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

		if (userAccount.getFromValid() == null)
		{
			userAccount.setFromValid(LocalDateTime.now());
		}

		if (userAccount.getToValid() == null)
		{
			userAccount.setToValid(LocalDateTime.now().plusYears(1000));
		}

		return userMapper.insertUserAccount(CmModelMapperUtils.map(userAccount, UserAccountEntity.class));
	}

	public int createUserContactInfo(UserContactInfoDto userContactInfo)
	{
		return userMapper.insertUserContactInfo(CmModelMapperUtils.map(userContactInfo, UserContactInfoEntity.class));
	}

	public int createUserByCopy(Map<String, String> paramMap)
	{
		Integer sourceUSeq = new Integer(paramMap.get("uSeq"));

		UserDto user = getUser(sourceUSeq);
		user.setFirstName(CmStringUtils.trimToEmpty(paramMap.get("firstName")));
		user.setLastName(CmStringUtils.trimToEmpty(paramMap.get("lastName")));
		user.setUSeq(null);
		user.setModDate(null);
		user.setModUsername(null);

		Integer uSeq = saveUser(user);

		UserAccountDto userAccount = user.getUserAccount();
		userAccount.setUSeq(uSeq);
		userAccount.setUsername(CmStringUtils.trimToEmpty(paramMap.get("username")));
		userAccount.setPassword(CmStringUtils.trimToEmpty(paramMap.get("password")));
		userAccount.setFgAccountLocked(PlatformCommonVo.NO);
		userAccount.setFgPasswordLocked(PlatformCommonVo.YES);
		userAccount.setModDate(null);
		userAccount.setModUsername(null);

		saveUserAccount(userAccount);

		UserContactInfoDto userContactInfo = user.getUserContactInfo();
		userContactInfo.setUSeq(uSeq);
		userContactInfo.setModDate(null);
		userContactInfo.setModUsername(null);

		saveUserContactInfo(userContactInfo);

		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setUSeq(sourceUSeq);
		cItemSearch.setFgVisible(PlatformCommonVo.ALL);

		List<CItemDto> cItems = contentAuthorityService.getMyCItemHierarchy(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			List<Integer> cSeqs = new ArrayList<>();

			for (CItemDto cItem : cItems)
			{
				if (CmStringUtils.equals(cItem.getFgDelete(), PlatformCommonVo.YES))
				{
					continue;
				}

				cSeqs.add(cItem.getCSeq());
			}

			CItemUserRlDto cItemUserRl = new CItemUserRlDto();
			cItemUserRl.setUSeq(uSeq);
			cItemUserRl.setCSeqs(cSeqs);

			contentAuthorityService.save(cItemUserRl);
		}

		return uSeq;
	}

	public int modifyUser(UserDto user)
	{
		return userMapper.updateUser(CmModelMapperUtils.map(user, UserEntity.class));
	}

	public int modifyUserAccount(UserAccountDto userAccount)
	{
		//비밀번호 변경 이력 남기기
		if (CmStringUtils.isNotEmpty(userAccount.getPassword()))
		{
			UserDto user = getUser(userAccount.getUSeq());

			UserPasswordChangeHistoryDto userPasswordChangeHistory = new UserPasswordChangeHistoryDto();
			userPasswordChangeHistory.setUSeq(userAccount.getUSeq());
			userPasswordChangeHistory.setTimezone(user.getUserContactInfo().getTimezone());

			userPasswordChangeHistoryService.create(userPasswordChangeHistory);

			userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
		}

		if (userAccount.getFromValid() == null)
		{
			userAccount.setFromValid(LocalDateTime.now());
		}

		if (userAccount.getToValid() == null)
		{
			userAccount.setToValid(LocalDateTime.now().plusYears(1000));
		}

		return userMapper.updateUserAccount(CmModelMapperUtils.map(userAccount, UserAccountEntity.class));
	}

	public int modifyUserContactInfo(UserContactInfoDto userContactInfo)
	{
		return userMapper.updateUserContactInfo(CmModelMapperUtils.map(userContactInfo, UserContactInfoEntity.class));
	}

	public int lockUserAccount(Integer uSeq, String fgAccountLocked)
	{
		if (uSeq != null && CmStringUtils.isNotEmpty(fgAccountLocked))
		{
			List<Integer> uSeqs = new ArrayList<>();
			uSeqs.add(uSeq);

			return lockUserAccount(uSeqs, fgAccountLocked);
		}

		return 0;
	}

	public int lockUserAccount(List<Integer> uSeqs, String fgAccountLocked)
	{
		if (CollectionUtils.isNotEmpty(uSeqs))
		{
			int result = 0;

			for (Integer uSeq : uSeqs)
			{
				if (uSeq == null)
				{
					continue;
				}

				UserAccountEntity userAccount = new UserAccountEntity();
				userAccount.setUSeq(uSeq);
				userAccount.setFgAccountLocked(CmStringUtils.defaultString(fgAccountLocked, PlatformCommonVo.NO));

				result += userMapper.updateFgAccountLocked(userAccount);
			}

			return result;
		}

		return 0;
	}

	public int delete(List<Integer> uSeqs)
	{
		if (CollectionUtils.isNotEmpty(uSeqs))
		{
			int result = 0;

			for (Integer uSeq : uSeqs)
			{
				if (uSeq == null)
				{
					continue;
				}

				UserEntity user = new UserEntity();
				user.setUSeq(uSeq);

				if (userMapper.delete(user) > 0)
				{
					result += userMapper.updateUsernameToDelete(user);
				}
			}

			return result;
		}

		return 0;
	}

	public int saveUser(UserDto user)
	{
		if (user.getUSeq() != null)
		{
			return modifyUser(user);
		}
		else
		{
			return createUser(user);
		}
	}

	public int saveUserAccount(UserAccountDto userAccount)
	{
		UserAccountEntity userAccountEntity = userMapper.findUserAccount(userAccount.getUSeq());

		if (userAccountEntity == null)
		{
			return createUserAccount(userAccount);
		}
		else
		{
			return modifyUserAccount(userAccount);
		}
	}

	public int saveUserContactInfo(UserContactInfoDto userContactInfo)
	{
		UserContactInfoEntity userContactInfoEntity = userMapper.findUserContactInfo(userContactInfo.getUSeq());

		if (userContactInfoEntity == null)
		{
			return createUserContactInfo(userContactInfo);
		}
		else
		{
			return modifyUserContactInfo(userContactInfo);
		}
	}

	public UserDto getUserByUsername(String username)
	{
		return getUser(new UserSearchDto(username));
	}

	public UserDto getUser(Integer uSeq)
	{
		return getUser(new UserSearchDto(uSeq));
	}

	public UserDto getUser(UserSearchDto userSearch)
	{
		return CmModelMapperUtils.map(userMapper.findUser(userSearch), UserDto.class);
	}

	public List<UserDto> getPageableUsers(UserSearchDto userSearch)
	{
		return CmModelMapperUtils.mapAll(userMapper.findPageUsers(userSearch), UserDto.class);
	}

	public List<UserDto> getUsersInCItem(UserSearchDto userSearch)
	{
		return CmModelMapperUtils.mapAll(userMapper.findUsersInCItem(userSearch), UserDto.class);
	}
}
