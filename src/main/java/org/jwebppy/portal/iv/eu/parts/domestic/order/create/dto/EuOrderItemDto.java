package org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderItemDto extends EuGeneralDto
{
	private String lineNo;
	private String higherLineNo;//Higher-level item in bill of material structures
	private String materialNo;
	private String materialName;
	private String supplyMaterialNo;
	private String description;
	private String plant;
	private String vendor;//LIFNR
	private String originOrderQty;
	private String orderQty;
	private String availability;
	private String minOrderQty;
	private String lotQty;
	private String costCode;
	private String netPrice;//NET_PRICE
	private String netValue;//NET_VALUE
	private String listPrice;//LIST
	private String uom;//UOM
	private String stockDiv;
	private String stockDiveu;
	private String stockDivuk;
	private String message;
	private String errorType;
	private String errorCode;
	private String errorMessage;

	public String getAdjustedOrderQty()
	{
		if (CmStringUtils.isNotEmpty(orderQty) && CmStringUtils.isNotEmpty(lotQty))
		{
			int iOrderQty = (int)Double.parseDouble(orderQty.replaceAll(",", ""));
			int iLotQty = (int)Double.parseDouble(lotQty.replaceAll(",", ""));

			iOrderQty = (iOrderQty == 0) ? 1 : iOrderQty;
			iLotQty = (iLotQty == 0) ? 1 : iLotQty;

			if (iOrderQty % iLotQty > 0)
			{
				return Integer.toString(iLotQty * ((iOrderQty / iLotQty) + 1));
			}

			return Integer.toString(iOrderQty);
		}

		return orderQty;
	}
}
