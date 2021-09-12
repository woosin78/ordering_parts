package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;

@NoLogging
public interface UserPasswordChangeHistoryMapper
{
	public int insertUserPasswordChangeHistory(UserPasswordChangeHistoryEntity userPasswordChangeHistory);
}
