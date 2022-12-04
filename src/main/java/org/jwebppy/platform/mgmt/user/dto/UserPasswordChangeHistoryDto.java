package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class UserPasswordChangeHistoryDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = 4033181503111344831L;

	private Integer useq;
	private String oldPassword;
	private String reason;
	private String timezone;
}
