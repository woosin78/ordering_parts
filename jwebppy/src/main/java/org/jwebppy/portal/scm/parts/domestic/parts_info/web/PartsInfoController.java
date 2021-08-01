package org.jwebppy.portal.scm.parts.domestic.parts_info.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.scm.parts.PartsErpDataMap;
import org.jwebppy.portal.scm.parts.PartsGeneralController;
import org.jwebppy.portal.scm.parts.domestic.parts_info.service.PartsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/scm/parts/domestic/info")
public class PartsInfoController extends PartsGeneralController
{
	@Autowired
	private PartsInfoService partsInfoService;

	@RequestMapping("/parts_info")
	public String partsInfoList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/parts_info/data")
	@ResponseBody
	public Object partInfoData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartsNo = CmStringUtils.trimToEmpty(paramMap.get("pPartsNo"));

		if ("".equals(pPartsNo))
		{
			return null;
		}

		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", pPartsNo.toUpperCase());//PartsNo

		RfcResponse rfcResponse2 = partsInfoService.getPartsStandardM(rfcParamMap);
		DataList items = rfcResponse2.getTable("T_RESULT");

		FormatBuilder.with(items)
		.undelimite("STOCK", ",")
		.decimalFormat("STOCK", "#,##0")
		.undelimite("STOCKP", ",")
		.decimalFormat("STOCKP", "#,##0")
		.decimalFormat("NTGEW", "#,##0.00");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("items", items);

		return resultMap;
	}

	@RequestMapping("/autocompete/data")
	@ResponseBody
	public Object autocomplete(@RequestParam Map<String, Object> paramMap)
	{
		String pPartsNo = CmStringUtils.trimToEmpty(paramMap.get("pPartsNo"));

		if ("".equals(pPartsNo))
		{
			return null;
		}

		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", pPartsNo.toUpperCase());//PartsNo

		DataList dataList = partsInfoService.getSimplePartsInfo(rfcParamMap);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("items", dataList);

		return resultMap;
	}

	@RequestMapping("/parts_number_popup_list")
	public String partsNumberList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}


	@RequestMapping("/parts_number_popup_list/data")
	@ResponseBody
	public Object partsNumberData(@RequestParam Map<String, Object> paramMap)
	{
		if (CmStringUtils.isEmpty(paramMap.get("pPartsNo")) && CmStringUtils.isEmpty(paramMap.get("pPartsNoDesc")))
		{
			return null;
		}

		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));			//PartsNo
		rfcParamMap.put("partsNoDesc", paramMap.get("pPartsNoDesc"));	//PartsNoDesc

		RfcResponse rfcResponse = partsInfoService.getPartsNumberList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("ZSVV0006");

		return dataList;
	}

	@RequestMapping("/sub_item_popup_list")
	public String subItemList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/sub_item_popup_list/data")
	@ResponseBody
	public Object subItemData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));			//PartsNo
		rfcParamMap.put("partsNoDesc", paramMap.get("pPartsNoDesc"));	//PartsNoDesc

		RfcResponse rfcResponse = partsInfoService.getSubItemList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_CROSS");

		return dataList;
	}

	@RequestMapping("/applied_model_popup_list")
	public String appliedModelList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/applied_model_popup_list/data")
	@ResponseBody
	public Object appliedModelData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));			//PartsNo
		rfcParamMap.put("productType", paramMap.get("pProductType"));	//PartsNoDesc

		RfcResponse rfcResponse = partsInfoService.getAppliedModelList(rfcParamMap);

		return rfcResponse.getTable("T_MODEL");
	}

	@RequestMapping("/alternative_popup_list")
	public String alternativeList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/alternative_popup_list/data")
	@ResponseBody
	public Object alternativeData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));			//PartsNo

		RfcResponse rfcResponse = partsInfoService.getAlternativeList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_MODEL");

		return dataList;
	}
}
