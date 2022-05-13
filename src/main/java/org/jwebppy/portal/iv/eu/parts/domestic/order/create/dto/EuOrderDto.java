package org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto;

import java.util.List;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderDto extends EuGeneralDto
{
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
	private List<EuOrderItemDto> orderItems;

	//시뮬레이션 시 주문 화면에서 전달 받는 파라미터. 구분자 '^'
	private String lineNo;
	private String materialNo;
	private String orderQty;
	private String minOrderQty;
	private String lotQty;
	private String uom;
	private String availability;

}
