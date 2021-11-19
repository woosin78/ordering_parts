package org.jwebppy.platform.mgmt.mail.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.common.service.MgmtGeneralService;
import org.jwebppy.platform.mgmt.mail.dto.Attachment;
import org.jwebppy.platform.mgmt.mail.dto.MailReceiverDto;
import org.jwebppy.platform.mgmt.mail.dto.MailSenderDto;
import org.jwebppy.platform.mgmt.mail.dto.ReceiverType;
import org.jwebppy.platform.mgmt.mail.entity.MailSendHistoryEntity;
import org.jwebppy.platform.mgmt.mail.mapper.MailSendHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class MailSenderService extends MgmtGeneralService
{
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private MailSendHistoryMapper mailSenderHistoryMapper;

	@Autowired
	private MailReceiverService mailReceiverService;

	public void send(MailSenderDto mailSender)
	{
		MailSendHistoryEntity mailSendHistory = save(mailSender);

		String from = mailSender.getFrom();
		List<String> to = mailSender.getTo();
		String subject = mailSender.getSubject();

		Set<String> toSet = new HashSet<>();

		if (CmStringUtils.isNotEmpty(from) && CollectionUtils.isNotEmpty(to) && CmStringUtils.isNotEmpty(subject))
		{
			try
			{
				for (String email: to)
				{
					if (toSet.contains(email))
					{
						continue;
					}

					toSet.add(email);

					MimeMessage mimeMessage = javaMailSender.createMimeMessage();
					MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

					mimeMessageHelper.setSubject(subject);
					mimeMessageHelper.setFrom(from, mailSender.getSender());

					if (CollectionUtils.isNotEmpty(mailSender.getAttachments()))
					{
						for (Attachment attachment: mailSender.getAttachments())
						{
							mimeMessageHelper.addAttachment(attachment.getName(), new FileSystemResource(attachment.getFile()), new MimetypesFileTypeMap().getContentType(attachment.getFile()));
						}
					}

					MailReceiverDto mailReceiver = new MailReceiverDto();
					mailReceiver.setMshSeq(mailSendHistory.getMshSeq());
					mailReceiver.setType(ReceiverType.TO);
					mailReceiver.setEmail(email);

					String text = addTrackingTag(mailSendHistory.getMshSeq(), email, mailSender.getText());

					try
					{
						if (CmStringUtils.equals(mailSender.getFgHtmlText(), PlatformCommonVo.YES))
						{
							mimeMessageHelper.setText(text, true);
						}
						else
						{
							mimeMessageHelper.setText(text);
						}

						mimeMessageHelper.setTo(email);
						javaMailSender.send(mimeMessage);
					}
					catch (MailException e)
					{
						mailReceiver.setError(ExceptionUtils.getStackTrace(e));
					}
					finally
					{
						mailReceiverService.save(mailReceiver);
					}
				}

				mailSendHistory.setCompleteDate(LocalDateTime.now());
			}
			catch (MessagingException | UnsupportedEncodingException e)
			{
				mailSendHistory.setError(ExceptionUtils.getStackTrace(e));
			}
			finally
			{
				mailSenderHistoryMapper.updateResult(mailSendHistory);
			}
		}
	}

	public MailSendHistoryEntity save(MailSenderDto mailSender)
	{
		MailSendHistoryEntity mailSendHistory = new MailSendHistoryEntity();

		mailSendHistory.setSubject(mailSender.getSubject());
		mailSendHistory.setText(mailSender.getText());
		mailSendHistory.setFrom(mailSender.getFrom());

		if (CollectionUtils.isNotEmpty(mailSender.getAttachments()))
		{
			List<Attachment> attachments = mailSender.getAttachments();

			StringBuilder str = new StringBuilder();

			for (int i=0, size=attachments.size(); i<size; i++)
			{
				Attachment attachment = attachments.get(i);

				if (i > 0)
				{
					str.append(", ");
				}

				str.append(CmStringUtils.defaultString(attachment.getName(), attachment.getFile().getName()));
			}

			mailSendHistory.setAttachment(str.toString());
		}

		mailSenderHistoryMapper.insert(mailSendHistory);

		return mailSendHistory;
	}

	private String addTrackingTag(Integer mshSeq, String email, String text)
	{
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		String key = null;

		try
		{
			key = URLEncoder.encode(AES256Cipher.getInstance().encode(mshSeq + PlatformConfigVo.DELIMITER + email), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		String tag = "<img src='" + httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + "/mail/tracking?key=" + key + "' style='display:none' />";

		return text + tag;
	}
}
