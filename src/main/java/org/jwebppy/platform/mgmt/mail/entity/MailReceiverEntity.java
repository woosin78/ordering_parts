package org.jwebppy.platform.mgmt.mail.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.jwebppy.platform.mgmt.mail.dto.ReceiverType;

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
public class MailReceiverEntity extends MgmtGeneralEntity
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
