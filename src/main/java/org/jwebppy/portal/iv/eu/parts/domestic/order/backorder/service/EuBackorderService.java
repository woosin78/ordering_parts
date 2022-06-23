package org.jwebppy.portal.iv.eu.parts.domestic.order.backorder.service;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.eu.parts.domestic.common.service.EuPartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EuBackorderService extends EuPartsDomesticGeneralService
{
	@Cacheable(value = PortalCacheConfig.BACKORDER, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_BACKORDER_LIST");

		String fromDate = paramMap.getString("fromDate");
		String toDate = paramMap.getString("toDate");

		if (paramMap.isAnyEmptyValue(new String[] {"orderNo", "poNo", "orderPartNo", "orderType"}))
		{
    		fromDate = "19000101";
    		toDate = "29001231";
		}

    	rfcRequest.addField("I_AUART", paramMap.getString("orderType"));
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_BSTKD", paramMap.getString("poNo"));	//P.O No.
		rfcRequest.addField("I_FDATE", fromDate);
		rfcRequest.addField("I_KUNNR", paramMap.getCustomerNo());
		rfcRequest.addField("I_MATNR", paramMap.getString("orderPartNo").toUpperCase());	// Order Part No.
		rfcRequest.addField("I_TDATE", toDate);
		rfcRequest.addField("I_USERID", paramMap.getUsername());
    	rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));	// Order No.

		return simpleRfcTemplate.response(rfcRequest);
	}
}
