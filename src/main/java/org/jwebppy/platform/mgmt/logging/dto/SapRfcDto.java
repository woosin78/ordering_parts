package org.jwebppy.platform.mgmt.logging.dto;

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
public class SapRfcDto extends MgmtGeneralDto
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
