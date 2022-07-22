package org.jwebppy.portal.iv.hq.parts.export.order.backorder.service;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExBackorderService extends PartsExportGeneralService
{
	@Cacheable(value = PortalCacheConfig.IVEX_BACKORDER, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		String fromDate = paramMap.getString("fromDate");
		String toDate = paramMap.getString("toDate");

		if (paramMap.isAnyEmptyValue(new String[] {"orderNo", "poNo", "orderPartNo", "orderType"}))
		{
    		fromDate = "19000101";
    		toDate = "29991231";
		}

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_BACKORDER_LIST");

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_FDATE", fromDate},
					{"I_TDATE", toDate},
					{"I_MATNR", paramMap.getString("orderPartNo").toUpperCase()}
				})
				.addByKey(new Object[][] {
					{"I_AUART", "orderType"},
					{"I_VBELN", "orderNo"},
					{"I_BSTKD", "poNo"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
			.output("T_BACKORDER_LIST");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
