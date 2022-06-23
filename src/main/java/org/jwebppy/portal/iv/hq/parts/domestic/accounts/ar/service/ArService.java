package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ar.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ArService extends PartsDomesticGeneralService
{
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_AP_INVOICE_DETAIL");

		rfcRequest
			.field().with(paramMap)
				.add("I_MATNR", paramMap.getString("partNo").toUpperCase())
				.addByKey(new Object[][] {
					{"I_F_EBELN", "fromPoNo"},
					{"I_T_EBELN", "toPoNo"},
					{"I_F_BELNR", "fromInvoiceNo"},
					{"I_T_BELNR", "toInvoiceNo"},
					{"I_F_BLDAT", "fromDate"},
					{"I_T_BLDAT", "toDate"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
			.output("T_AP_INVOICE_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
