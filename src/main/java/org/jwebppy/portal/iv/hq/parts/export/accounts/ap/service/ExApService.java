package org.jwebppy.portal.iv.hq.parts.export.accounts.ap.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExApService extends PartsExportGeneralService
{
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_INVOICE_LIST");

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_VKORG", paramMap.getSalesOrg()},
					{"I_VTWEG", paramMap.getDistChannel()},
					{"I_SPART", paramMap.getDivision()},
					{"I_KUNNR", paramMap.getCustomerNo()},
					{"I_USERID", paramMap.getUsername()}
				})
				.addByKey(new Object[][] {
					{"I_F_VBELN", "fromOrderNo"},
					{"I_T_VBELN", "toOrderNo"},
					{"I_F_ZFCIVNO", "fromInvoiceNo"},
					{"I_T_ZFCIVNO", "toInvoiceNo"},
					{"I_F_BLDAT", "fromDate"},
					{"I_T_BLDAT", "toDate"}
				})
			.and()
			.output("T_INVOICE1");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
