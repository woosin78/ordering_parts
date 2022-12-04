package org.jwebppy.platform.core.security.authentication.service;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.security.authentication.dto.LogoutHistoryDto;
import org.jwebppy.platform.core.security.authentication.entity.LogoutHistoryEntity;
import org.jwebppy.platform.core.security.authentication.mapper.LogoutHistoryMapper;
import org.jwebppy.platform.core.security.authentication.mapper.LogoutHistoryObjectMapper;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogoutHistoryService extends PlatformGeneralService
{
	@Autowired
	private LogoutHistoryMapper logoutHistoryMapper;

	public int createLogoutHistory(LogoutHistoryDto logoutHistory)
	{
		LogoutHistoryEntity logoutHistoryEntity = CmModelMapperUtils.mapToEntity(LogoutHistoryObjectMapper.INSTANCE, logoutHistory);

		logoutHistoryMapper.insertLogoutHistory(logoutHistoryEntity);

		return logoutHistoryEntity.getUseq();
	}
}
