package org.jwebppy.portal.iv.eu.parts.domestic.info.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.info.service.EuPartsInfoService;
import org.jwebppy.portal.iv.eu.parts.domestic.order.EuOrderGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH  + "/info")
public class EuPartsInfoController extends EuOrderGeneralController
{
	@Autowired
	private LangService langService;

	@Autowired
	private EuPartsInfoService partsInfoService;

	@RequestMapping("/part_info_list")
	@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
	public String partsInfoList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/sub/part_info_list")
	public String partsInfoListForSubDealer(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/part_info_list/data")
	@ResponseBody
	public Object partInfoData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartsNo = CmStringUtils.trimToEmpty(paramMap.get("pPartsNo"));

		if ("".equals(pPartsNo))
		{
			return null;
		}

		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", pPartsNo.toUpperCase());//PartsNo
		rfcParamMap.put("reqQty", paramMap.get("pReqQty"));//ReqQty

		RfcResponse rfcResponse2 = partsInfoService.getPartsStandardM(rfcParamMap);
		DataList plants = rfcResponse2.getTable("T_PLANT");
		DataList items = rfcResponse2.getTable("T_RESULT");

		FormatBuilder.with(items)
		.decimalFormat("NTGEW", "#,##0.00");

		if (CollectionUtils.isNotEmpty(items))
		{
			DataMap dataMap = (DataMap)items.get(0);

			if (CollectionUtils.isNotEmpty(plants))
			{
				DataMap plantMap = (DataMap)plants.get(0);
				rfcParamMap.put("plant", plantMap.get("WERKS"));
				dataMap.put("WERKS", plantMap.get("WERKS"));
			}

			if (dataMap.isNotEquals("SVRCD_S", "N"))
			{
				if (dataMap.isEquals("STOCKP", "P") || dataMap.isEquals("STOCKP", "N"))
				{
					String leadMsg = langService.getText("DIVEU_MSG_0011");

					if (dataMap.isEquals("LIFNR", "PF160"))
					{
						int reqQty = CmNumberUtils.toInt(CmStringUtils.defaultIfEmpty(paramMap.get("pReqQty"), "1"), 1);
						double stockDiv = dataMap.getDouble("DIV", 0);
						double stockDiveu = dataMap.getDouble("DIEUFL", 0);
						double stockQty = stockDiv + stockDiveu;

						if (reqQty <= stockQty)
						{
							leadMsg = langService.getText("DIVEU_MSG_0009");

							dataMap.put("LEAD_EXTRA_MSG", langService.getText("DIVEU_MSG_0010"));
						}
					}

					dataMap.put("LEAD_MSG", leadMsg);
				}
			}
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("items", items);


		// Certified Program 구함.
		// parameters : partsNo, plant
		RfcResponse rfcResponse = partsInfoService.getAmPartList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_ITEM");

		if (CollectionUtils.isNotEmpty(dataList)) {
			for (int i = 0; i<dataList.size(); i++){
				DataMap dataMap = (DataMap)dataList.get(i);
				if (!pPartsNo.toUpperCase().equals(dataMap.getString("AM_PART_NO").toUpperCase())){
					resultMap.put("CertifiedProgram", dataMap.getString("AM_PART_NO"));
					break;
				}
			}

		}

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

	@RequestMapping("/am_part_popup_list")
	public String amPartList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/am_part_popup_list/data")
	@ResponseBody
	public Object amPartData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));			//PartsNo
		rfcParamMap.put("plant", paramMap.get("pPlant"));

		RfcResponse rfcResponse = partsInfoService.getAmPartList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_ITEM");

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
		DataList dataList = rfcResponse.getTable("T_MODEL");

		return dataList;
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
