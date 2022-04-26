package org.jwebppy.portal.iv.hq.parts.domestic.order.delivery.service;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeliveryStatusService extends PartsDomesticGeneralService
{
	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.DELIVERY_STATUS, unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPMENT_LIST");

		rfcRequest.
			field().with(paramMap)
				.add(new Object[][] {
					{"I_USERID", paramMap.getUsername()},
					{"I_LANGU", paramMap.getLangForSap()}
				})
				.addByKey(new Object[][] {
					{"I_FDATE", "fromDate"},
					{"I_TDATE", "toDate"},
					{"I_FGDAT", "fromOnboardDate"},
					{"I_TGDAT", "toOnboardDate"},
					{"I_TKNUM", "shipmentNo"},
					{"I_VBELN", "orderNo"},
					{"I_MATNR", "partNo"},
					{"I_BSTNK", "poNo"}
				})
			.and()
			.output("T_LIST");

		return simpleRfcTemplate.response(rfcRequest);
	}

	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.DELIVERY_STATUS, unless="#result == null")
	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPMENT_DETAIL");

		rfcRequest.
		field().with(paramMap)
			.add(new Object[][] {
				{"I_USERID", paramMap.getUsername()},
				{"I_LANGU", paramMap.getLangForSap()}
			})
			.addByKey(new Object[][] {
				{"I_FDATE", "fromDate"},
				{"I_TDATE", "toDate"},
				{"I_FGDAT", "fromOnboardDate"},
				{"I_TGDAT", "toOnboardDate"},
				{"I_TKNUM", "shipmentNo"},
				{"I_VBELN", "orderNo"},
				{"I_MATNR", "partNo"},
				{"I_BSTNK", "poNo"}
			})
		.and()
		.output("T_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
