package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.conn_resource.entity.SapConnResourceEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupEntity extends GeneralDto implements IPagination
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

	public UserGroupEntity() {}

	public UserGroupEntity(Integer ugSeq)
	{
		this.ugSeq = ugSeq;
	}
}
