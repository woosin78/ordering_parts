package org.jwebppy.platform.core.security.authentication.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginHistoryDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = 1329218112737899890L;

	private String username;
	private String sessionId;
	private String ip;
	private String referer;
	private String userAgent;
	private String fgResult;
	private Integer uSeq;
	private String fgAccountLocked;
	private LocalDateTime accountLockedDate;
}
