package org.jwebppy.platform.mgmt.mail.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.mgmt.mail.dto.ReceiverType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailReceiverEntity extends GeneralEntity
{
	private static final long serialVersionUID = 8736704808198841689L;

	private Integer mrSeq;
	private Integer mshSeq;
	private ReceiverType type;
	private String email;
	private String error;
	private LocalDateTime sendDate;
	private LocalDateTime readDate;
}
