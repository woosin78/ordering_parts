package org.jwebppy.platform.core.security.authentication.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogoutHistoryDto extends GeneralDto
{
	private static final long serialVersionUID = -2670652458161535402L;

	private Integer uSeq;
	private String sessionId;
	private String referer;
	private String timezone;
}
