package org.jwebppy.platform.security.authentication.mapper;

import org.jwebppy.platform.security.authentication.entity.LogoutHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutHistoryMapper
{
	public void insertLogoutHistory(LogoutHistoryEntity logoutHistory);
}
