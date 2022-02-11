package org.jwebppy.portal.iv.hq.parts.domestic.report.order.service;

import java.util.Map;

import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.BwParseHelper;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OrderReportService extends PartsGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@Cacheable(value = CacheConfig.BUSINESS_TOOLS, key = "#paramMap", unless="#result == null")
	public Map<String, Object> getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("IV_BPP", "RS_VC_GET_QUERY_VIEW_DATA_FLAT");

		BwParseHelper bwParseHelper = new BwParseHelper();

        rfcRequest
        	.field()
        		.add(new Object[][] {
        			{"I_QUERY", "Z_ZSS_M001_Q004_D"}
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
