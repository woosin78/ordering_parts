package org.jwebppy.portal.iv.hq.parts.domestic.order.delivery.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryStatusService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPMENT_LIST");

		rfcRequest.addField("I_FDATE", paramMap.getString("fromDate"));
		rfcRequest.addField("I_TDATE", paramMap.getString("toDate"));
		rfcRequest.addField("I_FGDAT", paramMap.getString("fromOnboardDate"));
		rfcRequest.addField("I_TGDAT", paramMap.getString("toOnboardDate"));
		rfcRequest.addField("I_TKNUM", paramMap.getString("shipmentNo"));
		rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));
		rfcRequest.addField("I_MATNR", paramMap.getString("partNo"));
		rfcRequest.addField("I_BSTNK", paramMap.getString("poNo"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_LANGU", paramMap.getLang());

		rfcRequest.addOutputParameter("T_LIST");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPMENT_DETAIL");

		rfcRequest.addField("I_FDATE", paramMap.getString("fromDate"));
		rfcRequest.addField("I_TDATE", paramMap.getString("toDate"));
		rfcRequest.addField("I_FGDAT", paramMap.getString("fromOnboardDate"));
		rfcRequest.addField("I_TGDAT", paramMap.getString("toOnboardDate"));
		rfcRequest.addField("I_TKNUM", paramMap.getString("shipmentNo"));
		rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));
		rfcRequest.addField("I_MATNR", paramMap.getString("partNo"));
		rfcRequest.addField("I_BSTNK", paramMap.getString("poNo"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_LANGU", paramMap.getLang());

		rfcRequest.addOutputParameter("T_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
