package org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto;

import java.util.List;

import org.jwebppy.portal.iv.hq.common.HqCommonVo;
import org.jwebppy.portal.iv.hq.common.dto.HqGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnetimeAddressDto extends HqGeneralDto
{
	private static final long serialVersionUID = -7422066171691306770L;

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
	private String fgUse = HqCommonVo.NO;
	private String fgDelete = HqCommonVo.NO;
	private List<Integer> oaSeqs;
}
