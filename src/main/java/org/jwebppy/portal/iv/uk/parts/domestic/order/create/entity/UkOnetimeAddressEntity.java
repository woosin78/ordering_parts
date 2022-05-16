package org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity;

import org.jwebppy.portal.iv.uk.common.UkCommonVo;
import org.jwebppy.portal.iv.uk.common.entity.UkGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UkOnetimeAddressEntity extends UkGeneralEntity
{
	private static final long serialVersionUID = -3069535899548452267L;

	private Integer oaSeq;
	private Integer[] oaSeqs;
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
	private String fgUse = UkCommonVo.NO;
	private String fgDelete = UkCommonVo.NO;
}
