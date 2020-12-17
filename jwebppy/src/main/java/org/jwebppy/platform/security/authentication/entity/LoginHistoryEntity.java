package org.jwebppy.platform.security.authentication.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = -2953109150988945919L;
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
