package org.jwebppy.platform.mgmt.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
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
import org.jwebppy.platform.mgmt.user.mapper.UserAccountObjectMapper;
import org.jwebppy.platform.mgmt.user.mapper.UserContactInfoObjectMapper;
import org.jwebppy.platform.mgmt.user.mapper.UserMapper;
import org.jwebppy.platform.mgmt.user.mapper.UserObjectMapper;
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

	protected int createUser(UserDto user)
	{
		UserEntity userEntity = CmModelMapperUtils.mapToEntity(UserObjectMapper.INSTANCE, user);

		userMapper.insertUser(userEntity);

		return userEntity.getUseq();
	}

	protected int createUserAccount(UserAccountDto userAccount)
	{
		userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

		if (ObjectUtils.isEmpty(userAccount.getFromValid()))
		{
			userAccount.setFromValid(CmDateTimeUtils.now().toLocalDateTime());
		}

		if (ObjectUtils.isEmpty(userAccount.getToValid()))
		{
			userAccount.setToValid(CmDateTimeUtils.now().plusYears(1000).toLocalDateTime());
		}

		return userMapper.insertUserAccount(CmModelMapperUtils.mapToEntity(UserAccountObjectMapper.INSTANCE, userAccount));
	}

	protected int createUserContactInfo(UserContactInfoDto userContactInfo)
	{
		return userMapper.insertUserContactInfo(CmModelMapperUtils.mapToEntity(UserContactInfoObjectMapper.INSTANCE, userContactInfo));
	}

	public int createUserByCopy(Map<String, String> paramMap)
	{
		Integer sourceUSeq = Integer.valueOf(paramMap.get("useq"));

		UserDto user = getUser(sourceUSeq);
		user.setFirstName(CmStringUtils.trimToEmpty(paramMap.get("firstName")));
		user.setLastName(CmStringUtils.trimToEmpty(paramMap.get("lastName")));
		user.setUseq(null);
		user.setModDate(null);
		user.setModUsername(null);

		Integer useq = saveUser(user);

		UserAccountDto userAccount = user.getUserAccount();
		userAccount.setUseq(useq);
		userAccount.setUsername(CmStringUtils.trimToEmpty(paramMap.get("username")));
		userAccount.setPassword(CmStringUtils.trimToEmpty(paramMap.get("password")));
		userAccount.setFgAccountLocked(MgmtCommonVo.NO);
		userAccount.setFgPasswordLocked(MgmtCommonVo.YES);
		userAccount.setFromValid(LocalDateTime.now());
		userAccount.setToValid(LocalDateTime.now().plusYears(100));
		userAccount.setModDate(null);
		userAccount.setModUsername(null);

		saveUserAccount(userAccount);

		UserContactInfoDto userContactInfo = user.getUserContactInfo();
		userContactInfo.setUseq(useq);
		userContactInfo.setCountry(CmStringUtils.defaultString(userContactInfo.getCountry(), MgmtCommonVo.DEFAULT_COUNTRY));
		userContactInfo.setTimezone(CmStringUtils.defaultString(userContactInfo.getTimezone(), MgmtCommonVo.DEFAULT_TIMEZONE));
		userContactInfo.setModDate(null);
		userContactInfo.setModUsername(null);

		saveUserContactInfo(userContactInfo);

		CItemSearchDto citemSearch = new CItemSearchDto();
		citemSearch.setUseq(sourceUSeq);

		List<CItemDto> citems = contentAuthorityService.getMyCitems(citemSearch);

		if (CollectionUtils.isNotEmpty(citems))
		{
			List<Integer> cseqs = new ArrayList<>();

			for (CItemDto citem : citems)
			{
				cseqs.add(citem.getCseq());
			}

			contentAuthorityService.save(CItemUserRlDto.builder()
					.useq(useq)
					.cseqs(cseqs)
					.build());
		}

		return useq;
	}

	private int modifyUser(UserDto user)
	{
		return userMapper.updateUser(CmModelMapperUtils.mapToEntity(UserObjectMapper.INSTANCE, user));
	}

	protected int modifyUserAccount(UserAccountDto userAccount)
	{
		//비밀번호 변경 이력 남기기
		if (CmStringUtils.isNotEmpty(userAccount.getPassword()))
		{
			UserDto user = getUser(userAccount.getUseq());

			userPasswordChangeHistoryService.create(UserPasswordChangeHistoryDto.builder()
					.useq(userAccount.getUseq())
					.timezone(user.getTimezone())
					.build());

			userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
		}

		if (ObjectUtils.isEmpty(userAccount.getFromValid()))
		{
			userAccount.setFromValid(LocalDateTime.now());
		}

		if (ObjectUtils.isEmpty(userAccount.getToValid()))
		{
			userAccount.setToValid(LocalDateTime.now().plusYears(1000));
		}

		return userMapper.updateUserAccount(CmModelMapperUtils.mapToEntity(UserAccountObjectMapper.INSTANCE, userAccount));
	}

	protected int modifyUserContactInfo(UserContactInfoDto userContactInfo)
	{
		return userMapper.updateUserContactInfo(CmModelMapperUtils.mapToEntity(UserContactInfoObjectMapper.INSTANCE, userContactInfo));
	}

	public int lockUserAccount(Integer useq, String fgAccountLocked)
	{
		if (useq != null && CmStringUtils.isNotEmpty(fgAccountLocked))
		{
			List<Integer> useqs = new ArrayList<>();
			useqs.add(useq);

			return lockUserAccount(useqs, fgAccountLocked);
		}

		return 0;
	}

	public int lockUserAccount(List<Integer> useqs, String fgAccountLocked)
	{
		if (CollectionUtils.isNotEmpty(useqs))
		{
			int result = 0;

			for (Integer useq : useqs)
			{
				if (ObjectUtils.isEmpty(useq))
				{
					continue;
				}

				result += userMapper.updateFgAccountLocked(UserAccountEntity.builder()
						.useq(useq)
						.fgAccountLocked(CmStringUtils.defaultString(fgAccountLocked, MgmtCommonVo.NO))
						.build());
			}

			return result;
		}

		return 0;
	}

	public int deleteUser(List<Integer> useqs)
	{
		if (CollectionUtils.isNotEmpty(useqs))
		{
			int result = 0;

			for (Integer useq : useqs)
			{
				if (ObjectUtils.isEmpty(useq))
				{
					continue;
				}

				UserEntity user = UserEntity.builder()
						.useq(useq)
						.build();

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
		if (ObjectUtils.isEmpty(user.getUseq()))
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
		if (ObjectUtils.isEmpty(userMapper.findUserAccount(userAccount.getUseq())))
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
		UserContactInfoEntity userContactInfoEntity = userMapper.findUserContactInfo(userContactInfo.getUseq());

		if (ObjectUtils.isEmpty(userContactInfoEntity))
		{
			return createUserContactInfo(userContactInfo);
		}
		else
		{
			return modifyUserContactInfo(userContactInfo);
		}
	}

	public int resetPassword(List<Integer> useqs)
	{
		if (CollectionUtils.isNotEmpty(useqs))
		{
			for (Integer useq: useqs)
			{
				UserDto user = getUser(useq);

				UserAccountDto userAccount = user.getUserAccount();
				userAccount.setPassword(PlatformConfigVo.INITIAL_PASSWORD);
				userAccount.setFgPasswordLocked(MgmtCommonVo.YES);

				modifyUserAccount(userAccount);
			}

			return 1;
		}

		return 0;
	}

	public UserDto getUserByUsername(String username)
	{
		return getUser(new UserSearchDto(username));
	}

	public UserDto getUser(Integer useq)
	{
		return getUser(new UserSearchDto(useq));
	}

	public UserDto getUser(UserSearchDto userSearch)
	{
		return CmModelMapperUtils.mapToDto(UserObjectMapper.INSTANCE, userMapper.findUser(userSearch));
	}

	public List<UserDto> getUsersByUsernames(String[] usernames)
	{
		return getPageableUsers(UserSearchDto.builder()
				.usernames(Arrays.asList(usernames))
				.rowPerPage(99999)
				.build());
	}

	public List<UserDto> getPageableUsers(UserSearchDto userSearch)
	{
		return CmModelMapperUtils.mapToDto(UserObjectMapper.INSTANCE, userMapper.findPageableUsers(userSearch));
	}

	public List<UserDto> getUsersInCItem(UserSearchDto userSearch)
	{
		return CmModelMapperUtils.mapToDto(UserObjectMapper.INSTANCE, userMapper.findUsersInCItem(userSearch));
	}

	public boolean isExistByUsername(String username)
	{
		return (ObjectUtils.isNotEmpty(getUserByUsername(username))) ? true : false;
	}
}
