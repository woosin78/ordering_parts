package org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto;

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
public class SimulationResultDto extends OrderDto
{
	private static final long serialVersionUID = -1805714249334047867L;

	private String credit;//CREDIT
	private String creditCurrency;
	private String totalWeight;//TOTAL_WEIGHT
	private String weightUnit;
	private String fgChargeDocumentFree;
	private String blockReason;
	private String blockMessage;

	private List<OrderItemDto> normalOrderItems = new LinkedList<>();
	private List<OrderItemDto> errorOrderItems = new LinkedList<>();
	private List<PricingResultDto> pricingResults = new LinkedList<>();

	public void setNormalOrderItems(List<OrderItemDto> normalOrderItems)
	{
		this.normalOrderItems = normalOrderItems;

		if (CollectionUtils.isNotEmpty(normalOrderItems))
		{
			for (OrderItemDto orderItem : normalOrderItems)
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
