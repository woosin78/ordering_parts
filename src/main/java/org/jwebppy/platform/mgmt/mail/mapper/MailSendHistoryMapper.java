package org.jwebppy.platform.mgmt.mail.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.mail.entity.MailSendHistoryEntity;

@NoLogging
@Mapper
public interface MailSendHistoryMapper
{
	public int insert(MailSendHistoryEntity mailSender);
	public int updateResult(MailSendHistoryEntity mailSender);
}
