package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ap.service;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ApService extends PartsDomesticGeneralService
{
	@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.AP_LIST, unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_CLEARED_AR_BY_DAY2");

		Map<String, Object> budatMap = new HashMap<>();
		budatMap.put("SIGN", "I");
		budatMap.put("OPTION", "BT");
		budatMap.put("LOW", paramMap.get("fromDate"));
		budatMap.put("HIGH", paramMap.get("toDate"));

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_BUKRS", paramMap.getCorpNo()},
					{"I_LOCAL", "X"},
					{"I_USERID", paramMap.getUsername()}
				})
				.and()
				.structure("I_ORG")
					.add(new Object[][] {
						{"VKORG", paramMap.getSalesOrg()},
						{"VTWEG", paramMap.getDistChannel()},
						{"SPART", paramMap.getDivision()}
					})
			.and()
			.table("I_BUDAT")
				.add(budatMap)
			.output(new String[] {"LS_9188", "T_LIST"});

		return simpleRfcTemplate.response(rfcRequest);
	}

	@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.AP_DETAIL, unless="#result == null")
	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_INVOICE_DETAIL_LIST");

		rfcRequest
			.field().with(paramMap)
				.add("I_USERID", paramMap.getUsername())
				.addByKey(new Object[][] {
					{"I_F_ZFCIVNO", "fromInvoiceNo"},
					{"I_T_ZFCIVNO", "toInvoiceNo"},
					{"I_MATNR", "partNo"},
					{"I_TKNUM", "shipmentNo"},
					{"I_F_FKDAT", "fromDate"},
					{"I_T_FKDAT", "toDate"}
				})
			.and()
			.output("T_INVOICE_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
