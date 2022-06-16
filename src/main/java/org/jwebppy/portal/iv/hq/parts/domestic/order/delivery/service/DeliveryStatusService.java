package org.jwebppy.portal.iv.hq.parts.domestic.order.delivery.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryStatusService extends PartsDomesticGeneralService
{
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPMENT_LIST");

		rfcRequest.
			field().with(paramMap)
				.add("I_LANGU", paramMap.getLangForSap())
				.addByKey(new Object[][] {
					{"I_FDATE", "fromDate"},
					{"I_TDATE", "toDate"},
					{"I_FGDAT", "fromOnboardDate"},
					{"I_TGDAT", "toOnboardDate"},
					{"I_TKNUM", "shipmentNo"},
					{"I_VBELN", "orderNo"},
					{"I_MATNR", "partNo"},
					{"I_BSTNK", "poNo"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
			.output("T_LIST");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPMENT_DETAIL");

		rfcRequest.
		field().with(paramMap)
			.add("I_LANGU", paramMap.getLangForSap())
			.addByKey(new Object[][] {
				{"I_FDATE", "fromDate"},
				{"I_TDATE", "toDate"},
				{"I_FGDAT", "fromOnboardDate"},
				{"I_TGDAT", "toOnboardDate"},
				{"I_TKNUM", "shipmentNo"},
				{"I_VBELN", "orderNo"},
				{"I_MATNR", "partNo"},
				{"I_BSTNK", "poNo"}
			})
		.and()
		.structure("I_INPUT")
			.add(SimpleRfcMakeParameterUtils.me(paramMap))
		.output("T_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
