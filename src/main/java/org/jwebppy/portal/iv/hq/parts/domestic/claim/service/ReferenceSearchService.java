package org.jwebppy.portal.iv.hq.parts.domestic.claim.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ReferenceSearchService extends PartsDomesticGeneralService
{
	public RfcResponse getInvoiceList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_INVOICELIST");

		rfcRequest
			.structure("LS_SEARCH")
				.add(new String[][] {
					{"RFGSK", "X"},
					{"VBTYP", "C"},
					{"VBELN", paramMap.getString("VBELN")}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getOrderList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDERLIST");

		rfcRequest
			.structure("LS_SEARCH").with(paramMap)
				.add(new Object[][] {
					{"RFGSK", "X"},
					{"VBTYP", "C"},
					{"KVGR5", paramMap.getCustomerGrp5()}
				})
				.addByKey(new Object[][] {
					{"FRDATE", "fromDate"},
					{"TODATE", "toDate"},
					{"MATNR", "partNo"},
					{"VBELN", "orderNo"},
					{"BSTKD", "poNo"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}
}
