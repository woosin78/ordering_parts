package org.jwebppy.platform.mgmt.mail.mapper;

import org.jwebppy.platform.mgmt.mail.entity.MailReceiverEntity;

public interface MailReceiverMapper
{
	public int insert(MailReceiverEntity mailReceiver);
	public int updateReadDate(MailReceiverEntity mailReceiver);
}
