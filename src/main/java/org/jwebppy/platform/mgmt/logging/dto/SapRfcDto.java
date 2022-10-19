package org.jwebppy.platform.mgmt.logging.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SapRfcDto extends GeneralDto
{
	private static final long serialVersionUID = 6900717092233197569L;

	private Long srSeq;
	private String name;
	private String description;
	private int duration;
	private String fgUse;
	private LocalDateTime lastUsedDate;
	private String fgLoggingRequest;
	private String fgLoggingResult;
}
