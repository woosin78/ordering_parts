package org.jwebppy.portal.iv.eu.parts.order.create.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderHistoryHeaderDto extends EuGeneralDto
{
	private Integer ohhSeq;
	private String docType;
	private String orderType;
	private String soldToNo;
	private String shipToNo;
	private Integer oaSeq;
	private String salesOrg;
	private String distChannel;
	private String division;
	private String shippingCondition;
	private String poNo;
	private String soNo;
	private String errorMsg;
	private LocalDateTime orderedDate;
	private Integer duplOhhSeq;
	private List<EuOrderHistoryItemDto> orderHistoryItems;

	public void setHeader(EuOrderDto order)
	{
		this.docType = order.getDocType();
		this.orderType = order.getOrderType();
		this.soldToNo = order.getSoldToNo();
		this.shipToNo = order.getShipToNo();
		this.oaSeq = order.getOaSeq();
		this.salesOrg = order.getSalesOrg();
		this.distChannel = order.getDistChannel();
		this.division = order.getDivision();
		this.shippingCondition = order.getShippingCondition();
		this.poNo = order.getPoNo();
	}

	public void setItems(Integer ohhSeq, EuOrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			orderHistoryItems = new ArrayList<EuOrderHistoryItemDto>();

			for (EuOrderItemDto orderItem: order.getOrderItems())
			{
				EuOrderHistoryItemDto orderHistoryItem = new EuOrderHistoryItemDto();

				orderHistoryItem.setOhhSeq(ohhSeq);
				orderHistoryItem.setMaterialNo(orderItem.getMaterialNo());
				orderHistoryItem.setOrderQty(orderItem.getOrderQty());

				orderHistoryItems.add(orderHistoryItem);
			}
		}
	}

	public boolean isEquals(EuOrderDto order)
	{
		if (order == null)
		{
			return false;
		}

		if (
				CmStringUtils.equals(docType, order.getDocType()) &&
				CmStringUtils.equals(orderType, order.getOrderType()) &&
				CmStringUtils.equals(soldToNo, order.getSoldToNo()) &&
				CmStringUtils.equals(shipToNo, order.getShipToNo()) &&
				CmStringUtils.equals(CmStringUtils.defaultString(oaSeq), CmStringUtils.defaultString(order.getOaSeq())) &&
				CmStringUtils.equals(salesOrg, order.getSalesOrg()) &&
				CmStringUtils.equals(distChannel, order.getDistChannel()) &&
				CmStringUtils.equals(division, order.getDivision()) &&
				CmStringUtils.equals(shippingCondition, order.getShippingCondition()) &&
				CmStringUtils.equals(poNo, order.getPoNo())
				)
		{

			if (CollectionUtils.isEmpty(orderHistoryItems) || CollectionUtils.isEmpty(order.getOrderItems()))
			{
				return false;
			}

			if (orderHistoryItems.size() != order.getOrderItems().size())
			{
				return false;
			}

			int count = 0;
			for (EuOrderHistoryItemDto orderHistItem : orderHistoryItems)
			{
				for (EuOrderItemDto orderItem : order.getOrderItems())
				{
					if (
							CmStringUtils.equals(orderHistItem.getMaterialNo(), orderItem.getMaterialNo()) &&
							CmStringUtils.equals(orderHistItem.getOrderQty(), orderItem.getOrderQty())
							)
					{
						count++;
						break;
					}
				}
			}

			if (count != orderHistoryItems.size())
			{
				return false;
			}

			return true;
		}

		return false;
	}
}
