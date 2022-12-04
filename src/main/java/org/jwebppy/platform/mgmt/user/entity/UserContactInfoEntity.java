package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

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
public class UserContactInfoEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -2702559448170301038L;

	private Integer useq;
	private String email;
	private String tel1;
	private String tel2;
	private String tel3;
	private String mobile1;
	private String mobile2;
	private String mobile3;
	private String fax1;
	private String fax2;
	private String fax3;
	private String zipcode;
	private String street;
	private String city;
	private String state;
	private String country;
	private String timezone;
}
