package org.jwebppy.platform.core.security.authentication.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.entity.LoginHistoryEntity;

@NoLogging
@Mapper
public interface LoginHistoryMapper
{
	public int insertLoginHistory(LoginHistoryEntity loginHistoryEntity);
	public List<LoginHistoryEntity> findPageableLoginHistories(LoginHistorySearchDto loginHistorySearch);
	public int findLoginFailureCount(LoginHistorySearchDto loginHistorySearch);
}
