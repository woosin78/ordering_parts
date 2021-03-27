package org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto;

import java.util.List;

import org.jwebppy.portal.dbkr.DbkrCommonVo;
import org.jwebppy.portal.dbkr.DbkrGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnetimeAddressDto extends DbkrGeneralDto
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
	private String fgUse = DbkrCommonVo.NO;
	private String fgDelete = DbkrCommonVo.NO;
	private List<Integer> oaSeqs;
}
