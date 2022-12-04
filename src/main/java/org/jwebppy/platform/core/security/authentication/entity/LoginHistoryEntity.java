package org.jwebppy.platform.core.security.authentication.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.security.authentication.AuthenticationType;

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
public class LoginHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = -2953109150988945919L;

	private Integer lhSeq;
	private String username;
	private String sessionId;
	private String ip;
	private String referer;
	private String userAgent;
	//private LocalDateTime accountLockedDate;
	private Integer useq;
	private String fgResult;
	private String timezone;
	private AuthenticationType authenticationType;
}
