package org.jwebppy.platform.core.security.authentication.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

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
public class LogoutHistoryDto extends GeneralDto
{
	private static final long serialVersionUID = -2670652458161535402L;

	private Integer useq;
	private String sessionId;
	private String referer;
	private String timezone;
}
