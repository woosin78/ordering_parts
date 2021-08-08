package org.jwebppy.platform.mgmt.mail.mapper;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.mail.entity.MailReceiverEntity;

@NoLogging
public interface MailReceiverMapper
{
	public int insert(MailReceiverEntity mailReceiver);
	public int updateReadDate(MailReceiverEntity mailReceiver);
}
