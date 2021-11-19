package org.jwebppy.portal.iv.hq.parts.domestic.order.display.service;

import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OrderDisplayService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@Cacheable(value = CacheConfig.ORDER_DISPLAY, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDERLIST");

		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addStructure("LS_SEARCH", "KUNNR", paramMap.getCustomerNo());
		rfcRequest.addStructure("LS_SEARCH", "VKORG", paramMap.getSalesOrg());
		rfcRequest.addStructure("LS_SEARCH", "SPART", paramMap.getDivision());
		rfcRequest.addStructure("LS_SEARCH", "KVGR5", paramMap.getCustomerGrp5());
		rfcRequest.addStructure("LS_SEARCH", "VBTYP", paramMap.getString("docType"));
		rfcRequest.addStructure("LS_SEARCH", "AUART", paramMap.getString("orderType"));
		rfcRequest.addStructure("LS_SEARCH", "VBELN", paramMap.getString("orderNo"));
		rfcRequest.addStructure("LS_SEARCH", "BSTKD", paramMap.getString("poNo"));
		rfcRequest.addStructure("LS_SEARCH", "MATNR", paramMap.getString("orderPartNo"));
		rfcRequest.addStructure("LS_SEARCH", "RFGSK", paramMap.getString("status"));
		rfcRequest.addStructure("LS_SEARCH", "FRDATE", paramMap.getString("fromDate"));
		rfcRequest.addStructure("LS_SEARCH", "TODATE", paramMap.getString("toDate"));

		rfcRequest.addOutputParameter("LT_SEARCH");

		return simpleRfcTemplate.response(rfcRequest);
	}

	@Cacheable(value = CacheConfig.ORDER_DISPLAY_DETAIL, key = "#paramMap", unless="#result == null")
	public RfcResponse getDetail(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDER_LOAD");

		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("LV_REF_ORD", paramMap.getString("orderNo"));
		rfcRequest.addStructure("LS_IMPORT_PORTAL", "DOC_CATEGORY", paramMap.getString("docType", "C"));
		rfcRequest.addStructure("LS_IMPORT_PORTAL", "COMPLAINT", "N");
		rfcRequest.addStructure("LS_IMPORT_PORTAL", "DOC_MODE", "C");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getDownload(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest();

		if (paramMap.isEquals("mode", "Y006"))
		{
			rfcRequest.setFunctionName("ZSS_PARA_DIV_EP_SD_DOC_PRNT_V");
			rfcRequest.addField("KSCHL", paramMap.getString("mode"));
			rfcRequest.addField("LS_VBELN", paramMap.getString("orderNo"));
			rfcRequest.addStructure("CTL_OPTION", "LANGU", "EN");
			rfcRequest.addStructure("CTL_OPTION", "PDF_VIEW", "X");

			return simpleRfcTemplate.response(rfcRequest).getTable("T_PDF");
		}
		else
		{
			rfcRequest.setFunctionName("ZSS_PARA_DIV_EP_COMPLAIN_PRINT");
			rfcRequest.addField("I_USERID", paramMap.getUsername());
			rfcRequest.addField("I_LANGU", paramMap.getLang());
			rfcRequest.addField("ID", paramMap.getString("mode"));
			rfcRequest.addField("LV_REF_ORD", paramMap.getString("orderNo"));

			return simpleRfcTemplate.response(rfcRequest).getTable("LT_TAB");
		}
	}
}
