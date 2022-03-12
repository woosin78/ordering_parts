package org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.common.dto.PartsGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderItemDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 3626800808368640264L;

	private String lineNo;
	private String higherLineNo;//Higher-level item in bill of material structures
	private String materialNo;
	private String materialName;
	private String description;
	private String supplyMaterialNo;
	private String plant;
	private String vendor;//LIFNR
	private String orderQty;//주문 시 딜러가 입력 한 수량
	private String supplyQty;//공급 수량 - SAP 시뮬레이션 후 반환되는 수량
	private String availability;
	private String minOrderQty;
	private String lotQty;
	private String netPrice;
	private String netValue;
	private String listPrice;
	private String uom;
	private String amDevPart;
	private String boQty;
	private String stockDiv;
	private int subCount;//대체품 수
	private String message;
	private String errorType;
	private String errorCode;
	private String errorMessage;

	public String getAdjustedOrderQty()
	{
		if (CmStringUtils.isNotEmpty(this.orderQty) && CmStringUtils.isNotEmpty(this.lotQty))
		{
			int orderQty = Integer.parseInt(this.orderQty);
			int lotQty = (int)Double.parseDouble(this.lotQty);

			lotQty = (lotQty == 0) ? 1 : lotQty;

			if (orderQty % lotQty > 0)
			{
				return  Integer.toString(lotQty * ((orderQty / lotQty) + 1));
			}

			return this.orderQty;
		}

		return null;
	}

	public void incSubCount()
	{
		subCount++;
	}
}
