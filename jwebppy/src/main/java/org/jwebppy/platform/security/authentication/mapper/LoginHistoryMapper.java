package org.jwebppy.platform.security.authentication.mapper;

import java.util.List;

import org.jwebppy.platform.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.security.authentication.entity.LoginHistoryEntity;

public interface LoginHistoryMapper
{
	public int insertLoginHistory(LoginHistoryEntity loginHistoryEntity);
	public List<LoginHistoryEntity> findPageableLoginHistories(LoginHistorySearchDto loginHistorySearch);
	public int findLoginFailureCount(LoginHistorySearchDto loginHistorySearch);
}
