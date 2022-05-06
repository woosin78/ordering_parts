package org.jwebppy.portal.iv.hq.parts.export.report.order.machine.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExMachineOrderReportService extends PartsExportGeneralService
{
	public RfcResponse getList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_DEALER_SALES_STATUS");

		rfcRequest
			.field().with(paramMap)
				.add("I_USERID", paramMap.getUsername())
				.addByKey(new Object[][] {
					{"I_FPERIOD", "fromDate"},
					{"I_TPERIOD", "toDate"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
