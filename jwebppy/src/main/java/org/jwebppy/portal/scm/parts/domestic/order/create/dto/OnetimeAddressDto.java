package org.jwebppy.portal.scm.parts.domestic.order.create.dto;

import java.util.List;

import org.jwebppy.portal.scm.ScmCommonVo;
import org.jwebppy.portal.scm.ScmGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnetimeAddressDto extends ScmGeneralDto
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
	private String fgUse = ScmCommonVo.NO;
	private String fgDelete = ScmCommonVo.NO;
	private List<Integer> oaSeqs;
}
