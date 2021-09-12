package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserPasswordChangeHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = -1127244512720830947L;

	private Integer uSeq;
	private String oldPassword;
	private String reason;
	private String timezone;
}
