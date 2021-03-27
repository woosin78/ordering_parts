package org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.dbkr.scm.parts.PartsGeneralDto;

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
}
