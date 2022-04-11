package org.jwebppy.platform.mgmt.mail.dto;

import java.util.ArrayList;
import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailSenderDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -8377749717720382235L;

	private Integer mshSeq;
	private String subject;
	private String text;
	private String sender;
	private String fgHtmlText;
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private List<Attachment> attachments;
	private String error;

	public void setTo(List<String> to)
	{
		this.to = to;
	}

	public void addTo(String to)
	{
		if (CollectionUtils.isEmpty(this.to))
		{
			this.to = new ArrayList<>();
		}

		this.to.add(to);
	}

	public void setCc(List<String> cc)
	{
		this.cc = cc;
	}

	public void setCc(String cc)
	{
		if (CollectionUtils.isEmpty(this.cc))
		{
			this.cc = new ArrayList<>();
		}

		this.cc.add(cc);
	}

	public void setBcc(List<String> bcc)
	{
		this.bcc = bcc;
	}

	public void setBcc(String bcc)
	{
		if (CollectionUtils.isEmpty(this.bcc))
		{
			this.bcc = new ArrayList<>();
		}

		this.bcc.add(bcc);
	}

	public void setAttachments(List<Attachment> attachments)
	{
		this.attachments = attachments;
	}

	public void addAttachment(Attachment attachment)
	{
		if (CollectionUtils.isEmpty(this.attachments))
		{
			this.attachments = new ArrayList<>();
		}

		this.attachments.add(attachment);
	}
}
