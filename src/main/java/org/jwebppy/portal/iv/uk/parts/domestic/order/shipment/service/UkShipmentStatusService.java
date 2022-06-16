package org.jwebppy.portal.iv.uk.parts.domestic.order.shipment.service;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.uk.parts.domestic.common.service.UkPartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UkShipmentStatusService extends UkPartsDomesticGeneralService
{
	@Cacheable(value = PortalCacheConfig.SHIPMENT_STATUS, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SHIPMENT_INFO");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_BSTKD", paramMap.getString("poNo"));		// P.O.No.
		rfcRequest.addField("I_FKDAT", paramMap.getString("fromDate"));
		rfcRequest.addField("I_MATNR", paramMap.getString("partsNo"));			// Parts No
		rfcRequest.addField("I_TKDAT", paramMap.getString("toDate"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));			// Order No.

		return simpleRfcTemplate.response(rfcRequest);
	}

	@Cacheable(value = PortalCacheConfig.SHIPMENT_STATUS_DETAIL, key = "#paramMap", unless="#result == null")
	public RfcResponse getDetail(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SHIPMENT_INFO");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_FKDAT", paramMap.getString("fromDate"));
		rfcRequest.addField("I_TKDAT", paramMap.getString("toDate"));
		rfcRequest.addField("I_TKNUM", paramMap.getString("shipmentNo"));	// Shipment No
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getDownload(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_PDF_SHIP_DET");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_PORTAL", "X");
		rfcRequest.addField("I_TKNUM", paramMap.getString("shipmentNo"));	// Shipment No
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest).getTable("T_OUTTAB");
	}
}
