package org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto;

import java.util.List;

import org.jwebppy.portal.iv.hq.common.HqCommonVo;
import org.jwebppy.portal.iv.hq.common.dto.HqGeneralDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
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
	@Builder.Default
	private String fgUse = HqCommonVo.NO;
	@Builder.Default
	private String fgDelete = HqCommonVo.NO;
	private List<Integer> oaSeqs;
}
