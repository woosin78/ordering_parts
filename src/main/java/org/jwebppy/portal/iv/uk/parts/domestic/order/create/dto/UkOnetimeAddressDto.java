package org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto;

import java.util.List;

import org.jwebppy.portal.iv.uk.common.UkCommonVo;
import org.jwebppy.portal.iv.uk.common.dto.UkGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UkOnetimeAddressDto extends UkGeneralDto
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
	private String fgUse = UkCommonVo.NO;
	private String fgDelete = UkCommonVo.NO;
	private List<Integer> oaSeqs;
}
