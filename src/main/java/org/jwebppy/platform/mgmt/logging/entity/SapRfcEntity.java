package org.jwebppy.platform.mgmt.logging.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SapRfcEntity extends GeneralEntity
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
