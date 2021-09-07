package org.jwebppy.platform.mgmt.user.service;

import java.util.List;

import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.mapper.UserGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserGroupService extends GeneralService
{
	@Autowired
	private UserGroupMapper userGroupMapper;

	public UserGroupDto getUserGroup(Integer ugSeq)
	{
		return CmModelMapperUtils.map(userGroupMapper.findUserGroup(ugSeq), UserGroupDto.class);
	}

	public List<UserGroupDto> getUserGroups(UserGroupDto userGroup)
	{
		return CmModelMapperUtils.mapAll(userGroupMapper.findUserGroups(userGroup), UserGroupDto.class);
	}
}
