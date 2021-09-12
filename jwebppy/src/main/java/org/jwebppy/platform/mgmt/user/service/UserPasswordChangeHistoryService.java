package org.jwebppy.platform.mgmt.user.service;

import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;
import org.jwebppy.platform.mgmt.user.mapper.UserPasswordChangeHistoryMapper;
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
		return userPasswordChangeHistoryMapper.insertUserPasswordChangeHistory(CmModelMapperUtils.map(userPasswordChangeHistory, UserPasswordChangeHistoryEntity.class));
	}
}
