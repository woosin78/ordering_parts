package org.jwebppy.platform.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class ServletRequestInfoDto
{
	String requestUri;
	String requestId;
	String referer;
}
