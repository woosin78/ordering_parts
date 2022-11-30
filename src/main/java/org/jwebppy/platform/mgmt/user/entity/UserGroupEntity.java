package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.jwebppy.platform.mgmt.conn_resource.entity.SapConnResourceEntity;

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
public class UserGroupEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = -2864103624808776308L;

	private Integer ugSeq;
	private String name;
	private String description;
	private SapConnResourceEntity sapConnResource;
	private String dateFormat1;
	private String timeFormat1;
	private String dateFormat2;
	private String timeFormat2;
	private String country;
	private String timezone;
	private String currencyFormat;
	private String weightFormat;
	private String qtyFormat;
	private String langKind;
	private String defLang;
	private CredentialsPolicyEntity credentialsPolicy;
	private int userCount;

	public UserGroupEntity(Integer ugSeq)
	{
		this.ugSeq = ugSeq;
	}
}
