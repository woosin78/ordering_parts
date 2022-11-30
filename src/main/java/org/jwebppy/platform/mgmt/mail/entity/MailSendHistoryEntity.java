package org.jwebppy.platform.mgmt.mail.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class MailSendHistoryEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 6846433129570491719L;

	private Integer mshSeq;
	private String subject;
	private String text;
	private String sender;
	private String attachment;
	private LocalDateTime reserveDate;
	private String error;
	private LocalDateTime completeDate;
}
