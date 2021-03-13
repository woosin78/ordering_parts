package org.jwebppy.platform.mgmt.mail.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailSendHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = 6846433129570491719L;

	private Integer mshSeq;
	private String subject;
	private String text;
	private String from;
	private String attachment;
	private LocalDateTime reserveDate;
	private String error;
	private LocalDateTime completeDate;
}
