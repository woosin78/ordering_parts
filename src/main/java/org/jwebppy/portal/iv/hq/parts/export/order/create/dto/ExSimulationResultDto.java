package org.jwebppy.portal.iv.hq.parts.export.order.create.dto;

import java.util.LinkedList;
import java.util.List;

import org.jwebppy.platform.core.util.CmStringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExSimulationResultDto extends ExOrderDto
{
	private static final long serialVersionUID = -1805714249334047867L;

	private String credit;//CREDIT
	private String creditCurrency;
	private String totalWeight;//TOTAL_WEIGHT
	private String weightUnit;
	private String fgChargeDocumentFree;
	private String blockReason;
	private String blockMessage;

	private List<ExOrderItemDto> normalOrderItems = new LinkedList<>();
	private List<ExOrderItemDto> errorOrderItems = new LinkedList<>();
	private List<ExPricingResultDto> pricingResults = new LinkedList<>();

	public List<ExOrderItemDto> makeCompactNormalOrderItems(List<ExOrderItemDto> normalOrderItems)
	{
		List<ExOrderItemDto> orderItems = new LinkedList<>();

		for (int i=0, size=normalOrderItems.size(); i<size; i++)
		{
			ExOrderItemDto orderItem = normalOrderItems.get(i);

			if (CmStringUtils.equals(orderItem.getHigherLineNo(), "000000"))
			{
				for (int j=i+1; j<size; j++)
				{
					ExOrderItemDto subOrderItem = normalOrderItems.get(j);

					if (CmStringUtils.equals(orderItem.getLineNo(), subOrderItem.getHigherLineNo()))
					{
						orderItem.incSubCount();
					}
				}
			}
		}

		for (int i=0, size=normalOrderItems.size(); i<size; i++)
		{
			ExOrderItemDto orderItem = normalOrderItems.get(i);

			boolean isPartial = true;
			int subCount = orderItem.getSubCount();

			if (CmStringUtils.equals(orderItem.getHigherLineNo(), "000000") && subCount > 0)
			{
				for (int j=i+1, size2=i+subCount; j<=size2; j++)
				{
					ExOrderItemDto subOrderItem = normalOrderItems.get(j);

					if (CmStringUtils.equals(orderItem.getOrderQty(), subOrderItem.getSupplyQty()))
					{
						isPartial = false;
						break;
					}
				}

				if (isPartial)
				{
					for (int k=i, size2=i+subCount; k<=size2; k++)
					{
						orderItems.add(normalOrderItems.get(k));
					}
				}
				else
				{
					ExOrderItemDto subOrderItem = normalOrderItems.get(i + subCount);

					subOrderItem.setLineNo(orderItem.getLineNo());
					subOrderItem.setHigherLineNo("000000");
					subOrderItem.setMaterialNo(orderItem.getMaterialNo());
					subOrderItem.setMaterialName(orderItem.getMaterialName());
					subOrderItem.setSubCount(orderItem.getSubCount());

					orderItems.add(subOrderItem);
				}

				i = i + subCount;
			}
			else
			{
				orderItems.add(orderItem);
			}

			isPartial = true;
		}

		return orderItems;
	}
}
