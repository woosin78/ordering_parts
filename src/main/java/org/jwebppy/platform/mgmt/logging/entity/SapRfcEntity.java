package org.jwebppy.platform.mgmt.logging.entity;

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
public class SapRfcEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 5547927990009435159L;

	private Long srSeq;
	private String name;
	private String description;
	private int duration;
	private String fgUse;
	private LocalDateTime lastUsedDate;
	private String fgLoggingRequest;
	private String fgLoggingResult;
}
