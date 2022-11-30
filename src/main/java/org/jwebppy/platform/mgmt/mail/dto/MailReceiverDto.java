package org.jwebppy.platform.mgmt.mail.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class MailReceiverDto extends MgmtGeneralDto
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
