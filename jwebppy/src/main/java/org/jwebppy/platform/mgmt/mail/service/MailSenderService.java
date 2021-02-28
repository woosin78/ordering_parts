package org.jwebppy.platform.mgmt.mail.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.mail.dto.MailSenderDto;
import org.jwebppy.platform.mgmt.mail.entity.MailSendHistoryEntity;
import org.jwebppy.platform.mgmt.mail.mapper.MailSendHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService extends MgmtGeneralService
{
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private MailSendHistoryMapper mailSenderMapper;

	public void send(MailSenderDto mailSender) throws MessagingException
	{
		MailSendHistoryEntity mailSendHistory = saveSendHistory(mailSender);

		String from = mailSender.getFrom();
		List<String> to = mailSender.getTo();
		String subject = mailSender.getSubject();
		String text = mailSender.getText();

		if (CmStringUtils.isNotEmpty(from) && CollectionUtils.isNotEmpty(to) && CmStringUtils.isNotEmpty(subject) && CmStringUtils.isNotEmpty(text))
		{
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			try
			{
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setFrom(from, mailSender.getSender());
				mimeMessageHelper.setTo(toArray(to));

				if (CmStringUtils.equals(mailSender.getFgHtmlText(), PlatformCommonVo.YES))
				{
					mimeMessageHelper.setText(text, true);
				}
				else
				{
					mimeMessageHelper.setText(text);
				}

				if (CollectionUtils.isNotEmpty(mailSender.getCc()))
				{
					mimeMessageHelper.setCc(toArray(mailSender.getCc()));
				}

				if (CollectionUtils.isNotEmpty(mailSender.getBcc()))
				{
					mimeMessageHelper.setBcc(toArray(mailSender.getBcc()));
				}

				if (CollectionUtils.isNotEmpty(mailSender.getAttachments()))
				{
					for (File file: mailSender.getAttachments())
					{
						mimeMessageHelper.addAttachment("", new FileSystemResource(file));
					}
				}

				javaMailSender.send(mimeMessage);

				mailSendHistory.setCompleteDate(LocalDateTime.now());
			}
			catch (MessagingException | UnsupportedEncodingException | MailException e)
			{
				mailSendHistory.setError(ExceptionUtils.getStackTrace(e));
			}
			finally
			{
				mailSenderMapper.updateResult(mailSendHistory);
			}
		}
	}

	public MailSendHistoryEntity saveSendHistory(MailSenderDto mailSender)
	{
		MailSendHistoryEntity mailSendHistory = new MailSendHistoryEntity();

		mailSendHistory.setSubject(mailSender.getSubject());
		mailSendHistory.setText(mailSender.getText());
		mailSendHistory.setFrom(mailSender.getFrom());

		if (CollectionUtils.isNotEmpty(mailSender.getTo()))
		{
			mailSendHistory.setTo(mailSender.getTo().toString());
		}

		if (CollectionUtils.isNotEmpty(mailSender.getCc()))
		{
			mailSendHistory.setCc(mailSender.getCc().toString());
		}

		if (CollectionUtils.isNotEmpty(mailSender.getBcc()))
		{
			mailSendHistory.setBcc(mailSender.getBcc().toString());
		}

		if (CollectionUtils.isNotEmpty(mailSender.getAttachments()))
		{
			List<File> attachments = mailSender.getAttachments();

			String attachment = "[";

			for (int i=0, size=attachments.size(); i<size; i++)
			{
				File file = attachments.get(i);

				if (i > 0)
				{
					attachment += ", ";
				}

				attachment += file.getName();
			}

			attachment += "]";

			mailSendHistory.setAttachment(attachment);
		}

		mailSenderMapper.insert(mailSendHistory);

		return mailSendHistory;
	}

	private String[] toArray(List<String> list)
	{
		if (CollectionUtils.isEmpty(list))
		{
			return null;
		}

		return list.toArray(new String[list.size()]);
	}
}
