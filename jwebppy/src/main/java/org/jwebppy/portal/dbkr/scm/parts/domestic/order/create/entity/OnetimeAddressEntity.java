package org.jwebppy.portal.dbkr.scm.parts.domestic.order.create.entity;

import org.jwebppy.portal.dbkr.DbkrCommonVo;
import org.jwebppy.portal.dbkr.DbkrGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnetimeAddressEntity extends DbkrGeneralEntity
{
	private static final long serialVersionUID = -257265964143308569L;

	private Integer oaSeq;
	private String corp;
	private String customerNo;
	private String name;
	private String street;
	private String city;
	private String country;
	private String countryName;
	private String postalCode;
	private String transportZone;
	private String transportZoneName;
	private String fgUse = DbkrCommonVo.NO;
	private String fgDelete = DbkrCommonVo.NO;
}
