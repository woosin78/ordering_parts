package org.jwebppy.portal.iv.hq.parts.export.order.create.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.portal.iv.hq.parts.export.common.dto.PartsExportGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExOrderDto extends PartsExportGeneralDto
{
	private static final long serialVersionUID = -2913348250006096364L;

	private String docType;//VBTYP, SD document category
	private String orderType;//AUART
	private String priceGroup;//KONDA, Price group (customer)
	private String soldToNo;//KUNNR
	private String soldToName;//KUNNR_NAME
	private String soldToStreet;//KUNNR_STREE
	private String shipToNo;//KUNAG
	private String shipToName;//KUNAG_NAME
	private String shiptToAddress;
	private Integer oaSeq;//Onetime Address 식별자
	private String customerGroup5;//KVGR5
	private String poNo;//BSTNK
	private String rdd;//VDATU
	private String salesOrg;//VKORG
	private String distChannel;//VTWEG
	private String division;//SPART
	private String shippingCondition;//VSBED
	private String shippingConditionName;//VSBED_TEXT
	private String route;//ROUTE
	private String routeName;//ROUTE_TEXT
	private String paymentTerms;//ZTERM, Terms of Payment Key
	private String paymentTermsName;//ZTERM_TEXT, Terms of Payment Key
	private String incoterms1;//INCO1,Incoterms (Part 1)
	private String incoterms2;//INCO2,Incoterms (Part 2)
	private String totalAmount;//P_NETTX
	private String currency;//WAERS
	private String compleDelivery;
	private String remark;
	private String allowableNetValue;//ZZORAMT, EUFL:Free Transport 가 될 수 있는 최소 주문 금액
	private String simulationFrom;
	private String fileName;//엑셀 업로드 시뮬레이션 시 업로드 된 엑셀 파일 명
	private String username;
	private String language;
	private String fgShowSubstitute;
	private String fgShowAvailability;
	private String fgShowCredit;
	private String fgShowListPrice;
	private List<ExOrderItemDto> orderItems;
	private List<ExOrderItemDto> duplicateOrderItems;
	private List<ExOrderItemDto> invalidSalesLotOrderItems;
	private String fgFilteringDuplicateItem;
	private String fgFilteringInvalidSalesLotItem;

	//시뮬레이션 시 주문 화면에서 전달 받는 파라미터. 구분자 '^'
	private String lineNo;
	private String materialNo;
	private String orderQty;
	private String minOrderQty;
	private String lotQty;
	private String uom;
	private String availability;

	//중복 입력된 자재 필터링
	public void filteringDuplicateItems()
	{
		if (CollectionUtils.isNotEmpty(orderItems))
		{
			duplicateOrderItems = new ArrayList<>();
			List<ExOrderItemDto> tempOrderItems = new ArrayList<>();

			Set<String> itemSet = new HashSet<>();

			for (ExOrderItemDto orderItem: orderItems)
			{
				String materialNo = orderItem.getMaterialNo();

				if (itemSet.contains(materialNo))
				{
					orderItem.setErrorType("DUPL_MATNR");

					duplicateOrderItems.add(orderItem);
				}
				else
				{
					tempOrderItems.add(orderItem);
				}

				itemSet.add(materialNo);
			}

			orderItems = tempOrderItems;
		}
	}

	//Sales Lot 에 맞지 않게 수량을 입력한 자재 필터링
	public void filteringInvalidSalesLotOrderItems()
	{
		if (CollectionUtils.isNotEmpty(orderItems))
		{
			invalidSalesLotOrderItems = new ArrayList<>();
			List<ExOrderItemDto> tempOrderItems = new ArrayList<>();

			for (ExOrderItemDto orderItem: orderItems)
			{
				if (Double.parseDouble(orderItem.getOrderQty()) % Double.parseDouble(orderItem.getLotQty()) > 0)
				{
					invalidSalesLotOrderItems.add(orderItem);
				}
				else
				{
					tempOrderItems.add(orderItem);
				}
			}

			orderItems = tempOrderItems;
		}
	}
}
