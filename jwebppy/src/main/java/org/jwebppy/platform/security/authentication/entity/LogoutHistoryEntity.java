package org.jwebppy.platform.security.authentication.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogoutHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = 3410212927810508635L;

	private Integer uSeq;
	private String sessionId;
	private String referer;
}
