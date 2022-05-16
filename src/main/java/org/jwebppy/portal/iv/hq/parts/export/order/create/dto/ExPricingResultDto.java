package org.jwebppy.portal.iv.hq.parts.export.order.create.dto;

import org.jwebppy.portal.iv.hq.parts.export.common.dto.PartsExportGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExPricingResultDto extends PartsExportGeneralDto
{
	private static final long serialVersionUID = 2774711827389951920L;

	private String name;
	private String netValue;
	private String currency;
}
