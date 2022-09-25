package org.jwebppy.platform.core.security.authentication.dto;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.security.authentication.AuthenticationType;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
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

	private Integer lhSeq;
	private String username;
	private String sessionId;
	private String ip;
	private String referer;
	private String userAgent;
	//private LocalDateTime accountLockedDate;
	private Integer uSeq;
	private String fgResult;
	private String timezone;
	private AuthenticationType authenticationType;

	public String getRgResult()
	{
		return CmStringUtils.defaultIfEmpty(fgResult, PlatformCommonVo.NO);
	}

	public String getDisplayZonedRegDate()
	{
		return CmDateFormatUtils.format(regDate, timezone);
	}
}
