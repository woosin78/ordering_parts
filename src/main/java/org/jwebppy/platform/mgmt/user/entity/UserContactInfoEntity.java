package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserContactInfoEntity extends GeneralEntity
{
	private static final long serialVersionUID = -2702559448170301038L;
	private Integer uSeq;
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
