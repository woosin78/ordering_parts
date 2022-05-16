package org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto;

import org.jwebppy.portal.iv.uk.common.dto.UkGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UkPricingResultDto extends UkGeneralDto
{
	private String name;
	private String netValue;
	private String currency;
}
