package org.jwebppy.portal.iv.eu.parts.complaint.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.eu.parts.order.OrderGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UkComplaintDisplayService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDERLIST");

		rfcRequest.addField("COMPLAINT", "Y");
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		rfcRequest.addStructure("LS_SEARCH", "FRDATE", paramMap.getString("fromDate"));
		rfcRequest.addStructure("LS_SEARCH", "MATNR", paramMap.getString("partNo"));	// Part No
		rfcRequest.addStructure("LS_SEARCH", "TODATE", paramMap.getString("toDate"));
		rfcRequest.addStructure("LS_SEARCH", "VBELN", paramMap.getString("complaintNo"));	// Complaint No
		rfcRequest.addStructure("LS_SEARCH", "VBTYP", "A");
		rfcRequest.addStructure("LS_SEARCH", "VGBEL", paramMap.getString("referenceNo"));	// Reference No

		rfcRequest.addOutputParameter("LT_SEARCH2");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getComplaintReasonList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_COMPLAIN_REASON");

		rfcRequest.addField("LS_ZZCMPCD", paramMap.getString("reason1"));
		rfcRequest.addField("I_LANG", paramMap.getLang());

		rfcRequest.addOutputParameter("LT_VALUESET");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getDetail(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDER_LOAD");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("LV_REF_ORD", paramMap.getString("orderNo"));
		rfcRequest.addStructure("LS_IMPORT_PORTAL", "DOC_CATEGORY", paramMap.getString("docType"));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getFileDown(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_DOWNLOAD");

		rfcRequest.addField("LV_REF_ORD", paramMap.getString("orderNo"));
		rfcRequest.addField("DOCU_ITEM", paramMap.getString("docuItem"));
		rfcRequest.addField("INT_NO", paramMap.getString("intNo"));

		return simpleRfcTemplate.response(rfcRequest);
	}
}