package org.jwebppy.platform.core.security.authentication.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

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
public class LogoutHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = 3410212927810508635L;

	private Integer useq;
	private String sessionId;
	private String referer;
	private String timezone;
}
