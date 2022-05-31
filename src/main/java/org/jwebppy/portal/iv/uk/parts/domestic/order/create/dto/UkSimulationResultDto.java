package org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto;

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
public class UkSimulationResultDto extends UkOrderDto
{
	private static final long serialVersionUID = 815463611982604330L;

	private String credit;//CREDIT
	private String creditCurrency;
	private String totalWeight;//TOTAL_WEIGHT
	private String weightUnit;
	private String fgChargeDocumentFree;
	private String blockReason;
	private String blockMessage;

	private List<UkOrderItemDto> normalOrderItems = new LinkedList<>();
	private List<UkOrderItemDto> errorOrderItems = new LinkedList<>();
	private List<UkPricingResultDto> pricingResults = new LinkedList<>();

	public void setNormalOrderItems(List<UkOrderItemDto> normalOrderItems)
	{
		this.normalOrderItems = normalOrderItems;

		if (CollectionUtils.isNotEmpty(normalOrderItems))
		{
			for (UkOrderItemDto orderItem : normalOrderItems)
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
