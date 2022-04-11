package org.jwebppy.platform.mgmt.mail.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailReceiverDto extends GeneralDto
{
	private static final long serialVersionUID = 7701119175319483974L;

	private Integer mrSeq;
	private Integer mshSeq;
	private ReceiverType type;
	private String email;
	private String error;
	private LocalDateTime sendDate;
	private LocalDateTime readDate;
}
