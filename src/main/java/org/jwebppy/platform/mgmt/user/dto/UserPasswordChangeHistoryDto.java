package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserPasswordChangeHistoryDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = 4033181503111344831L;

	private Integer uSeq;
	private String oldPassword;
	private String reason;
	private String timezone;
}
