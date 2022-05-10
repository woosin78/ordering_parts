package org.jwebppy.portal.iv.eu.parts.order.create.dto;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuPricingResultDto extends EuGeneralDto
{
	private String name;
	private String netValue;
	private String currency;
}
