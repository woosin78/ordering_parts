package org.jwebppy.platform.mgmt.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.entity.UserPasswordChangeHistoryEntity;

@NoLogging
@Mapper
public interface UserPasswordChangeHistoryMapper
{
	public int insertUserPasswordChangeHistory(UserPasswordChangeHistoryEntity userPasswordChangeHistory);
	public List<UserPasswordChangeHistoryEntity> findPageUserPasswordChangeHistories(UserPasswordChangeHistoryDto userPasswordChangeHistory);
}
