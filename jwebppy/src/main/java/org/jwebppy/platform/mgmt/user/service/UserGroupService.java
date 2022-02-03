package org.jwebppy.platform.mgmt.user.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupSearchDto;
import org.jwebppy.platform.mgmt.user.entity.UserGroupEntity;
import org.jwebppy.platform.mgmt.user.mapper.UserGroupMapper;
import org.jwebppy.platform.mgmt.user.mapper.UserGroupObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserGroupService extends GeneralService
{
	@Autowired
	private UserGroupMapper userGroupMapper;

	public int save(UserGroupDto userGroup)
	{
		if (userGroup.getUgSeq() == null)
		{
			return create(userGroup);
		}
		else
		{
			return modify(userGroup);
		}
	}

	public int create(UserGroupDto userGroup)
	{
		UserGroupEntity userGroupEntity = CmModelMapperUtils.mapToEntity(UserGroupObjectMapper.INSTANCE, userGroup);

		userGroupMapper.insert(userGroupEntity);

		return userGroupEntity.getUgSeq();
	}

	public int modify(UserGroupDto userGroup)
	{
		return userGroupMapper.update(CmModelMapperUtils.mapToEntity(UserGroupObjectMapper.INSTANCE, userGroup));
	}

	public int delete(UserGroupEntity userGroup)
	{
		return userGroupMapper.updateFgDelete(userGroup);
	}

	public int delete(List<Integer> ugSeqs)
	{
		int result = 0;

		if (CollectionUtils.isNotEmpty(ugSeqs))
		{
			for (Integer ugSeq: ugSeqs)
			{
				result += delete(new UserGroupEntity(ugSeq));
			}
		}

		return result;
	}

	public UserGroupDto getUserGroup(Integer ugSeq)
	{
		return CmModelMapperUtils.mapToDto(UserGroupObjectMapper.INSTANCE, userGroupMapper.findUserGroup(ugSeq));
	}

	public UserGroupDto getUserGroupByName(String name)
	{
		return CmModelMapperUtils.mapToDto(UserGroupObjectMapper.INSTANCE, userGroupMapper.findUserGroupByName(name));
	}

	public List<UserGroupDto> getUserGroups(UserGroupSearchDto userGroupSearch)
	{
		return CmModelMapperUtils.mapToDto(UserGroupObjectMapper.INSTANCE, userGroupMapper.findUserGroups(userGroupSearch));
	}

	public List<UserGroupDto> getPageableUserGroups(UserGroupSearchDto userGroupSearch)
	{
		return CmModelMapperUtils.mapToDto(UserGroupObjectMapper.INSTANCE, userGroupMapper.findfindPageUserGroups(userGroupSearch));
	}

	public UserGroupDto getDefaultUserGroupIfEmpty(UserGroupSearchDto userGroupSearch)
	{
		return null;
	}
}
