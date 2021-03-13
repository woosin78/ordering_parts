package org.jwebppy.platform.core.security.authentication.mapper;

import org.jwebppy.platform.core.security.authentication.entity.LogoutHistoryEntity;

public interface LogoutHistoryMapper
{
	public void insertLogoutHistory(LogoutHistoryEntity logoutHistory);
}
