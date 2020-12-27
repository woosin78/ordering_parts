package org.jwebppy.platform.security.authentication.mapper;

import java.util.List;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.security.authentication.entity.LoginHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryMapper
{
	@NoLogging
	public int insertLoginHistory(LoginHistoryEntity loginHistoryEntity);
	public List<LoginHistoryEntity> findPageableLoginHistories(LoginHistorySearchDto loginHistorySearch);
	@NoLogging
	public int findLoginFailureCount(LoginHistorySearchDto loginHistorySearch);
}
