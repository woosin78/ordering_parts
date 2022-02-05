package org.jwebppy.portal.iv.hq.parts.domestic.report.order.service;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.BwParsingUtils;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderReportService extends PartsGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public Map<String, Object> getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("IV_BPP", "RS_VC_GET_QUERY_VIEW_DATA_FLAT");

		int seq = 1;

        rfcRequest
        	.field()
        		.add(new Object[][] {
        			{"I_QUERY", "Z_ZSS_M001_Q004_D"}
        		})
        	.and()
        	.table("I_T_PARAMETER")
        		.add(BwParsingUtils.range(seq++, "0SALEORG", paramMap.getSalesOrg()))
        		.add(BwParsingUtils.range(seq++, "ZC_OO001", paramMap.getDistChannel()))
        		.add(BwParsingUtils.single(seq++, "ZC_SM002", paramMap.getString("year")))
        		.add(BwParsingUtils.single(seq++, "ZC_SO044", paramMap.getCustomerNo()));

		return BwParsingUtils.parse(simpleRfcTemplate.response(rfcRequest));
	}
}
