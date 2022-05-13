package org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuSimulationResultDto extends EuOrderDto
{
	private String credit;//CREDIT
	private String creditCurrency;
	private String totalWeight;//TOTAL_WEIGHT
	private String weightUnit;
	private String fgChargeDocumentFree;
	private String blockReason;
	private String blockMessage;

	private List<EuOrderItemDto> normalOrderItems = new LinkedList<>();
	private List<EuOrderItemDto> errorOrderItems = new LinkedList<>();
	private List<EuPricingResultDto> pricingResults = new LinkedList<>();

	public void setNormalOrderItems(List<EuOrderItemDto> normalOrderItems)
	{
		this.normalOrderItems = normalOrderItems;

		if (CollectionUtils.isNotEmpty(normalOrderItems))
		{
			for (EuOrderItemDto orderItem : normalOrderItems)
			{
				if (CmStringUtils.startsWith(orderItem.getMessage(), "Dangerous"))
				{
					this.blockReason = "DANGEROUS";
					this.blockMessage = orderItem.getMessage();

					break;
				}
			}
		}
	}
}
