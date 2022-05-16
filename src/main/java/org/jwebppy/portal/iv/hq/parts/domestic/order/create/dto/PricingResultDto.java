package org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto;

import org.jwebppy.portal.iv.hq.parts.domestic.common.dto.PartsDomesticGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PricingResultDto extends PartsDomesticGeneralDto
{
	private static final long serialVersionUID = 2774711827389951920L;

	private String name;
	private String netValue;
	private String currency;
}
