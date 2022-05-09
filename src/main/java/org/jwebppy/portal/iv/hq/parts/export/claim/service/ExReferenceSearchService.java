package org.jwebppy.portal.iv.hq.parts.export.claim.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExReferenceSearchService extends PartsExportGeneralService
{
	public RfcResponse getInvoiceList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_INVOICELIST");

		rfcRequest
			.field()
				.add(new String[][] {
					{"I_BGTYP", "P"},
					{"I_USERID", paramMap.getUsername()}
				})
			.and()
			.structure("LS_SEARCH")
				.add(new String[][] {
					{"RFGSK", "X"},
					{"VBTYP", "C"},
					{"VBELN", paramMap.getString("VBELN")}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getOrderList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDERLIST");

		rfcRequest
			.field()
				.add(new String[][] {
					{"I_BGTYP", "P"},
					{"I_USERID", paramMap.getUsername()}
				})
			.and()
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
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

}
