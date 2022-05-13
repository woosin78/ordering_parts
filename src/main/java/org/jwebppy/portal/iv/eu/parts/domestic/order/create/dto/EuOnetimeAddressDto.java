package org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto;

import java.util.List;

import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EuOnetimeAddressDto extends EuGeneralDto
{
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
	private String fgUse = EuCommonVo.NO;
	private String fgDelete = EuCommonVo.NO;
	private List<Integer> oaSeqs;
}
