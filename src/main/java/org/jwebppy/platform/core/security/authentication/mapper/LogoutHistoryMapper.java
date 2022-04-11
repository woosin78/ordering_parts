package org.jwebppy.platform.core.security.authentication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.core.security.authentication.entity.LogoutHistoryEntity;

@NoLogging
@Mapper
public interface LogoutHistoryMapper
{
	public void insertLogoutHistory(LogoutHistoryEntity logoutHistory);
}
