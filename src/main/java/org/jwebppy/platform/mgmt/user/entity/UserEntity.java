package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
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
public class UserEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = -6209378005125200210L;

	private Integer useq;
	private String firstName;
	private String lastName;
	private String enFirstName;
	private String enLastName;
	private String company;
	private String organization;
	private String department;
	private String position;
	private String language;
	private UserAccountEntity userAccount;
	private UserContactInfoEntity userContactInfo;
	private UserGroupEntity userGroup;
}
