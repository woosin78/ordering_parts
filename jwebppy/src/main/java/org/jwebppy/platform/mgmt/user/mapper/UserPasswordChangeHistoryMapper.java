package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;

@NoLogging
public interface UserPasswordChangeHistoryMapper
{
	public int insertUserPasswordChangeHistory(UserPasswordChangeHistoryEntity userPasswordChangeHistory);
	public List<UserPasswordChangeHistoryDto> findPageUserPasswordChangeHistories(UserPasswordChangeHistoryDto userPasswordChangeHistory);
}
