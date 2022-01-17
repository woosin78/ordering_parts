package org.jwebppy.portal.iv.hq.parts.domestic.order.status.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	//@Cacheable(value = CacheConfig.ORDER_STATUS, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_OR_STATUS_INFO");

		String fromDate = paramMap.getString("fromDate");
		String toDate = paramMap.getString("toDate");

        if (!"".equals(paramMap.getString("orderNo")) || !"".equals(paramMap.getString("poNo")))
        {
        	fromDate = "20080101";
        	toDate = CmDateFormatUtils.unlimitDate(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD);
        }

        String orderType = paramMap.getString("orderType");
        String poNo = paramMap.getString("poNo");
        String custCode = paramMap.getCustomerNo();
        String orderPartNo = paramMap.getString("orderPartNo");
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

	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ST_INFO_DETAIL");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_DROP", "0");
		rfcRequest.addField("I_FDATE", "20080101");
		rfcRequest.addField("I_STATUS", "0");
		rfcRequest.addField("I_TDATE", CmDateFormatUtils.unlimitDate(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD));
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_DROP", "0");

		String[] orderNos = CmStringUtils.split(CmStringUtils.strip(paramMap.getString("orderNo"), PortalConfigVo.DELIMITER), PortalConfigVo.DELIMITER);

		if (orderNos.length > 1)
		{
			List<Map<String, Object>> orderNoList = new LinkedList<>();

			for (String orderNo: orderNos)
			{
				if (CmStringUtils.isNotEmpty(orderNo))
				{
					Map<String, Object> orderNoMap = new HashMap<>();
					orderNoMap.put("VBELN", orderNo);

					orderNoList.add(orderNoMap);
				}
			}

			if (CollectionUtils.isNotEmpty(orderNoList))
			{
				rfcRequest.addTable("LT_VBELN", orderNoList);
			}
		}
		else
		{
			rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));
		}

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getTotalDownload(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ST_INFO_DETAIL");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_DROP", "0");
		rfcRequest.addField("I_FDATE", "20080101");
		rfcRequest.addField("I_STATUS", "0");
		rfcRequest.addField("I_TDATE", CmDateFormatUtils.unlimitDate(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD));
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
