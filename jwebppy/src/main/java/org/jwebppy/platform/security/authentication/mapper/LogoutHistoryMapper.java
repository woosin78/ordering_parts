package org.jwebppy.platform.security.authentication.mapper;

import org.jwebppy.platform.security.authentication.entity.LogoutHistoryEntity;

public interface LogoutHistoryMapper
{
	public void insertLogoutHistory(LogoutHistoryEntity logoutHistory);
}
