package org.jwebppy.platform.security.authentication.service;

import org.jwebppy.platform.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.security.authentication.dto.LogoutHistoryDto;
import org.jwebppy.platform.security.authentication.entity.LogoutHistoryEntity;
import org.jwebppy.platform.security.authentication.mapper.LogoutHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogoutHistoryService extends PlatformGeneralService
{
	@Autowired
	private LogoutHistoryMapper logoutHistoryMapper;

	@Transactional
	public int createLogoutHistory(LogoutHistoryDto logoutHistory)
	{
		LogoutHistoryEntity logoutHistoryEntity = CmModelMapperUtils.map(logoutHistory, LogoutHistoryEntity.class);

		logoutHistoryMapper.insertLogoutHistory(logoutHistoryEntity);

		return logoutHistoryEntity.getUSeq();
	}
}
