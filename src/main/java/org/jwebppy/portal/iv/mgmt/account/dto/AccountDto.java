package org.jwebppy.portal.iv.mgmt.account.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AccountDto extends IvGeneralDto
{
	private static final long serialVersionUID = 7201555083863833923L;

	private Integer uSeq;
	private String bizType;
	private UserType userType;
	private String dealerCode;
	private String username;
	private String password;
	private String lastName;
	private String firstName;
}
