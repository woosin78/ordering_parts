package org.jwebppy.portal.iv.hq.parts.domestic.info.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public class PartsInfoService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getItemSubList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ITEM_SUB_LIST");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_MATNR", paramMap.getString("partsNo"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getPartsStandardM(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_PARTS_STANDARD");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_KVGR5", paramMap.getCustomerGrp5());
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_REQTY", paramMap.getString("reqQty"));

		List<Map<String, Object>> items = new LinkedList<>();

		Map<String, Object> item = new HashMap<>();
		item.put("MATNR", paramMap.getString("partsNo"));
		items.add(item);

		rfcRequest.addTable("T_MATNR", items);

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getPartsNumberList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MARA");

		rfcRequest.addField("ILANGU", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("MAKTX", paramMap.getString("partsNoDesc"));
		rfcRequest.addField("MATNR", paramMap.getString("partsNo"));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getSubItemList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ITEM_SUB_LIST");

		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_MATNR", paramMap.getString("partsNo"));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAppliedModelList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_APP_MODEL_LIST");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_KVGR5", paramMap.getString("productType"));
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_MATNR", paramMap.getString("partsNo"));
		rfcRequest.addField("I_MVGR3", paramMap.getString("productType"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAlternativeList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MULTI_SUBST");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_MATNR", paramMap.getString("partsNo"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest);
	}

	@Cacheable(value = CacheConfig.PARTS_INFO_AUTOCOMPLETE, key = "#paramMap", unless="#result == null")
	public DataList getSimplePartsInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY2");

		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		Map<String, Object> itemMap = new HashMap<>();
		itemMap.put("MATERIAL", paramMap.getString("partsNo"));

		List<Map<String, Object>> items = new LinkedList<>();
		items.add(itemMap);

		rfcRequest.addTable("LT_ITEM", items);

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}
}
