package org.jwebppy.portal.iv.hq.parts.domestic.report.order.service;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.BwParseHelper;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class OrderReportService extends PartsDomesticGeneralService
{
	public Map<String, Object> getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("IV_BPP", "RS_VC_GET_QUERY_VIEW_DATA_FLAT");

		BwParseHelper bwParseHelper = new BwParseHelper();

        rfcRequest
        	.field()
        		.add(new Object[][] {
        			{"I_QUERY", paramMap.getString("query")}
        		})
        	.and()
        	.table("I_T_PARAMETER")
        		.add(bwParseHelper.range("0SALEORG", paramMap.getSalesOrg()))
        		.add(bwParseHelper.range("ZC_OO001", paramMap.getDistChannel()))
        		.add(bwParseHelper.single("ZC_SM002", paramMap.getString("year")))
        		.add(bwParseHelper.single("ZC_SO044", paramMap.getCustomerNo()));

		return bwParseHelper.parse(simpleRfcTemplate.response(rfcRequest));
	}
}
