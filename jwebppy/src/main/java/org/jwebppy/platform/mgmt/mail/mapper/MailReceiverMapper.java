package org.jwebppy.platform.mgmt.mail.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.mail.entity.MailReceiverEntity;

@NoLogging
@Mapper
public interface MailReceiverMapper
{
	public int insert(MailReceiverEntity mailReceiver);
	public int updateReadDate(MailReceiverEntity mailReceiver);
}
