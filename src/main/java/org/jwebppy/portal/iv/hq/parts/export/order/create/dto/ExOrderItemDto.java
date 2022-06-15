package org.jwebppy.portal.iv.hq.parts.export.order.create.dto;

import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.dto.PartsExportGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExOrderItemDto extends PartsExportGeneralDto
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
	private String netWeight;
	private int subCount;//대체품 수
	private String message;
	private String errorType;
	private String errorCode;
	private String errorMessage;
	private String fgInvalidSalesLot = PortalCommonVo.NO;

	public String getAdjustedOrderQty()
	{
		if (CmStringUtils.isNotEmpty(this.orderQty) && CmStringUtils.isNotEmpty(this.lotQty))
		{
			int iOrderQty = (int)Double.parseDouble(this.orderQty.replaceAll(",", ""));
			int iLotQty = (int)Double.parseDouble(this.lotQty.replaceAll(",", ""));

			iOrderQty = (iOrderQty == 0) ? 1 : iOrderQty;
			iLotQty = (iLotQty == 0) ? 1 : iLotQty;

			if (iOrderQty % iLotQty > 0)
			{
				return Integer.toString(iLotQty * ((iOrderQty / iLotQty) + 1));
			}

			return Integer.toString(iOrderQty);
		}

		return null;
	}

	public void incSubCount()
	{
		subCount++;
	}

	public boolean isInvalidLotQty()
	{
		return (CmNumberUtils.toInt(orderQty, 1) % CmNumberUtils.toInt(lotQty, 1) > 0);
	}
}
