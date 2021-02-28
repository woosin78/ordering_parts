package org.jwebppy.platform.mgmt.mail.mapper;

import org.jwebppy.platform.mgmt.mail.entity.MailSendHistoryEntity;

public interface MailSendHistoryMapper
{
	public int insert(MailSendHistoryEntity mailSender);
	public int updateResult(MailSendHistoryEntity mailSender);
}
