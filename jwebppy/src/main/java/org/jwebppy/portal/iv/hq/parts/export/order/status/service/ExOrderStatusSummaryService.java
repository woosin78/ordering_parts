package org.jwebppy.portal.iv.hq.parts.export.order.status.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExOrderStatusSummaryService extends PartsExportGeneralService
{
	public RfcResponse getList(ErpDataMap paramMap)
	{
        RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDER_STAT_SUMMARY_REPORT");

        rfcRequest
        	.field().with(paramMap)
        		.add(new Object[][] {
        			{"I_USERID", paramMap.getUsername()},
        			{"I_UPLOAD", "X"}
        		})
        		.addByKey(new Object[][] {
        			{"I_VBELN", "orderNo"},
        			{"I_BSTKD", "poNo"},
        			{"I_MATNR", "orderPartNo"},
        			{"I_FDATE", "fromDate"},
        			{"I_TDATE", "toDate"},
        			{"I_STATUS", "status"}
        		});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
