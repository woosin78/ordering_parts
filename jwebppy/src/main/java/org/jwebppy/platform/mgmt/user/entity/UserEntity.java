package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserEntity extends GeneralEntity implements IPagination
{
	private static final long serialVersionUID = -6209378005125200210L;
	private Integer uSeq;
	private String firstName;
	private String lastName;
	private String enFirstName;
	private String enLastName;
	private String company;
	private String organization;
	private String department;
	private String position;
	private String language;
	private String fgDelete;
	private UserAccountEntity userAccount;
	private UserContactInfoEntity userContactInfo;

	public String getFgDelete()
	{
		return CmStringUtils.defaultString(fgDelete, PlatformCommonVo.NO);
	}
}
