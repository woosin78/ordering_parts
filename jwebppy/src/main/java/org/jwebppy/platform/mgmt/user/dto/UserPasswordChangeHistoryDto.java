package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserPasswordChangeHistoryDto extends GeneralDto
{
	private Integer uSeq;
	private String oldPassword;
	private String reason;
}
