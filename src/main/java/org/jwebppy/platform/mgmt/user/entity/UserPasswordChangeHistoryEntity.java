package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class UserPasswordChangeHistoryEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = -1127244512720830947L;

	private Integer uSeq;
	private String oldPassword;
	private String reason;
	private String timezone;
}
