package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CredentialsPolicyEntity extends GeneralEntity implements IPagination
{
	private static final long serialVersionUID = -5712552286897710540L;

	private Integer cpSeq;
	private UserGroupEntity userGroup;
	private String name;
	private String description;
	private String uMinLength;
	private String uMaxLength;
	private String uMinUppercase;
	private String uMaxUppercase;
	private String uMinLowercase;
	private String uMaxLowercase;
	private String uMinNumber;
	private String uMaxNumber;
	private String uMinSpecial;
	private String uMaxSpecial;
	private String pMinLength;
	private String pMaxLength;
	private String pMinUppercase;
	private String pMaxUppercase;
	private String pMinLowercase;
	private String pMaxLowercase;
	private String pMinNumber;
	private String pMaxNumber;
	private String pMinSpecial;
	private String pMaxSpecial;
	private String fgUse;
	private String fgDefault;
	private String fgDelete;
}
