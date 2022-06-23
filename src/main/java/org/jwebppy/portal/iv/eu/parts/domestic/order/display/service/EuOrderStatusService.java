package org.jwebppy.portal.iv.eu.parts.domestic.order.display.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.service.EuPartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EuOrderStatusService extends EuPartsDomesticGeneralService
{
	@Cacheable(value = PortalCacheConfig.ORDER_STATUS, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_OR_STATUS_INFO");

		String fromDate = paramMap.getString("fromDate");
		String toDate = paramMap.getString("toDate");

        if (!"".equals(paramMap.getString("orderNo")) || !"".equals(paramMap.getString("poNo")))
        {
        	fromDate = "20080101";
        	toDate = CmDateFormatUtils.format(LocalDateTime.now(), EuCommonVo.DEFAULT_DATE_YYYYMMDD_FORMAT).substring(0, 4) + "1231";
        }

        String orderType = paramMap.getString("orderType");
        String poNo = paramMap.getString("poNo");
        String custCode = paramMap.getCustomerNo();
        String orderPartNo = paramMap.getString("orderPartNo").toUpperCase();
        String orderNo = paramMap.getString("orderNo");

		rfcRequest.addField("I_AUART", orderType);
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_BSTKD", poNo);
		rfcRequest.addField("I_FDATE", fromDate);
		rfcRequest.addField("I_KUNNR", custCode);
		rfcRequest.addField("I_MATNR", orderPartNo);		// Order Part No.
		rfcRequest.addField("I_TDATE", toDate);
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_VBELN", orderNo);

		rfcRequest.addOutputParameter("T_HEADER");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getDetail(ErpDataMap paramMap)
	{
		String toDate = CmDateFormatUtils.format(LocalDateTime.now(), EuCommonVo.DEFAULT_DATE_YYYYMMDD_FORMAT).substring(0, 4) + "1231";

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ST_INFO_DETAIL");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_DROP", "0");
		rfcRequest.addField("I_FDATE", "20080101");
		rfcRequest.addField("I_STATUS", "0");
		rfcRequest.addField("I_TDATE", toDate);
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_DROP", "0");
		rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));

		List<Map<String, Object>> items = new LinkedList<>();
		Map<String, Object> itemMap = new HashMap<>();
		itemMap.put("VBELN", paramMap.getString("orderNo"));
		items.add(itemMap);
		rfcRequest.addTable("LT_VBELN", items);

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getMeaCustomerChk(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MEA_CUST_CHECK");

		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_BGTYP", "P");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getTotalDownload(ErpDataMap paramMap)
	{
		String toDate = CmDateFormatUtils.format(LocalDateTime.now(), EuCommonVo.DEFAULT_DATE_YYYYMMDD_FORMAT).substring(0, 4) + "1231";

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ST_INFO_DETAIL");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_DROP", "0");
		rfcRequest.addField("I_FDATE", "20080101");
		rfcRequest.addField("I_STATUS", "0");
		rfcRequest.addField("I_TDATE", toDate);
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		String[] orderNos = paramMap.getString("detailVBELN").split("@");
		List<Map<String, Object>> items = new LinkedList<>();

        for (int i=0, length=orderNos.length ; i<length; i++)
        {
			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("VBELN", orderNos[i]);
			items.add(itemMap);
        }

        rfcRequest.addTable("LT_VBELN", items);

        rfcRequest.addOutputParameter("T_DETAIL");

        return simpleRfcTemplate.response(rfcRequest);
	}
}
