package org.jwebppy.platform.mgmt.user.service;

import java.util.List;

import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.mapper.UserPasswordChangeHistoryMapper;
import org.jwebppy.platform.mgmt.user.mapper.UserPasswordChangeHistoryObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserPasswordChangeHistoryService extends GeneralService
{
	@Autowired
	private UserPasswordChangeHistoryMapper userPasswordChangeHistoryMapper;

	public int create(UserPasswordChangeHistoryDto userPasswordChangeHistory)
	{
		return userPasswordChangeHistoryMapper.insertUserPasswordChangeHistory(CmModelMapperUtils.mapToEntity(UserPasswordChangeHistoryObjectMapper.INSTANCE, userPasswordChangeHistory));
	}

	public List<UserPasswordChangeHistoryDto> getPageableUserPasswordChangeHistories(UserPasswordChangeHistoryDto userPasswordChangeHistory)
	{
		return CmModelMapperUtils.mapToDto(UserPasswordChangeHistoryObjectMapper.INSTANCE, userPasswordChangeHistoryMapper.findPageUserPasswordChangeHistories(userPasswordChangeHistory));
	}
}
