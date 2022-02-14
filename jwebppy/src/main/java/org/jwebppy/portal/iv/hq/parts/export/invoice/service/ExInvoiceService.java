package org.jwebppy.portal.iv.hq.parts.export.invoice.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExInvoiceService extends PartsExportGeneralService
{
	public RfcResponse getList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_SHIPMENT_FOR_INVOICE_HEAD");

		rfcRequest
			.field().with(paramMap)
			.add(new Object[][] {
				{"I_USERID", paramMap.getUsername()},
				{"I_VKORG", paramMap.getSalesOrg()},
				{"I_VTWEG", paramMap.getDistChannel()},
				{"I_SPART", paramMap.getDivision()},
				{"I_KUNAG", paramMap.getCustomerNo()}
			})
			.addByKey(new Object[][] {
				{"I_F_BLDAT", "fromDate"},
				{"I_T_BLDAT", "toDate"},
				{"I_F_ZFCIVNO", "invoiceNo"},
				{"I_BSTNK", "poNo"},
				{"I_MATWA", "orderPartNo"}
			})
			.output("T_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
