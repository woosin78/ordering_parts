package org.jwebppy.portal.dbkr.scm.parts.domestic.order.create.dto;

import org.jwebppy.portal.dbkr.scm.parts.PartsGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PricingResultDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 2774711827389951920L;

	private String name;
	private String netValue;
	private String currency;
}
