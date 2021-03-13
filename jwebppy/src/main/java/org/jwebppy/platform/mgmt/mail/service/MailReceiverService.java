package org.jwebppy.platform.mgmt.mail.service;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.mail.dto.MailReceiverDto;
import org.jwebppy.platform.mgmt.mail.entity.MailReceiverEntity;
import org.jwebppy.platform.mgmt.mail.mapper.MailReceiverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailReceiverService extends MgmtGeneralService
{
	@Autowired
	private MailReceiverMapper mailReceiverMapper;

	public Integer save(MailReceiverDto mailReceiver)
	{
		MailReceiverEntity mailReceiverEntity = CmModelMapperUtils.map(mailReceiver, MailReceiverEntity.class);

		mailReceiverMapper.insert(mailReceiverEntity);

		return mailReceiverEntity.getMrSeq();
	}

	public void modifyReadDate(Integer mshSeq, String email)
	{
		MailReceiverEntity mailReceiver = new MailReceiverEntity();
		mailReceiver.setMshSeq(mshSeq);
		mailReceiver.setEmail(email);
		mailReceiver.setReadDate(LocalDateTime.now());

		mailReceiverMapper.updateReadDate(mailReceiver);
	}
}
