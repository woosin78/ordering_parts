package org.jwebppy.portal.iv.hq.parts.domestic.info.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.info.service.PartInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/info")
public class PartInfoController extends PartsDomesticGeneralController
{
	@Autowired
	private PartInfoService partsInfoService;

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartNo = CmStringUtils.trimToEmpty(paramMap.get("pPartNo"));

		if ("".equals(pPartNo))
		{
			return EMPTY_RETURN_VALUE;
		}

		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partNo", pPartNo);//PartsNo

		RfcResponse rfcResponse = partsInfoService.getPartInfo(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_RESULT");

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			dataMap.put("LPRICE2", new BigDecimal(CmStringUtils.defaultIfEmpty(dataMap.get("LPRICE"), "0")).multiply(new BigDecimal("1.25")));
		}

		calcPriceByCurrency(dataList, new String[] { "LPRICE2", "LPRICE" }, "WAERS", new String[] { "KRW", "JPY" }, 100);

		FormatBuilder.with(dataList)
			.qtyFormat(new String[] { "OPENQ", "EXP_AVQ" })
			.decimalFormat(new String[] { "NTGEW", "BRGEW", "PLIFZ", "LPRICE2", "LPRICE" })
			.dateFormat("DATAB");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("items", dataList);

		return resultMap;
	}

	@RequestMapping("/autocompete/data")
	@ResponseBody
	public Object autocompleteData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartNo = CmStringUtils.trimToEmpty(paramMap.get("pPartNo"));

		if ("".equals(pPartNo))
		{
			return EMPTY_RETURN_VALUE;
		}

		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("partNo", pPartNo);//PartsNo

		return partsInfoService.getSimplePartInfo(rfcParamMap);
	}

	@RequestMapping("/popup/part_search")
	public String partSearch(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/part_search/data")
	@ResponseBody
	public Object partSearchData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartNo = CmStringUtils.defaultString(paramMap.get("pPartNo"));

		if (CmStringUtils.isEmpty(pPartNo) && CmStringUtils.isEmpty(paramMap.get("pPartDesc")))
		{
			return EMPTY_RETURN_VALUE;
		}

		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"partNo", "pPartNo"},
				{"partDesc", "pPartDesc"}
			});

		RfcResponse rfcResponse = partsInfoService.getPartList(rfcParamMap);

		return rfcResponse.getTable("ZSVV0006");
	}

	@RequestMapping("/popup/sub_part_list")
	public String subItemList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/sub_part_list/data")
	@ResponseBody
	public Object subItemListData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartNo = CmStringUtils.defaultString(paramMap.get("pPartNo"));

		if (CmStringUtils.isEmpty(pPartNo) && CmStringUtils.isEmpty(paramMap.get("pPartDesc")))
		{
			return EMPTY_RETURN_VALUE;
		}

		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"partNo", "pPartNo"},
				{"partDesc", "pPartDesc"}
			});

		RfcResponse rfcResponse = partsInfoService.getSubItemList(rfcParamMap);

		Map<String, DataList> resultMap = new HashMap<>();
		resultMap.put("crossList", rfcResponse.getTable("T_CROSS"));
		resultMap.put("partList", rfcResponse.getTable("T_LIST"));

		return resultMap;
	}

	@RequestMapping("/popup/applied_model_list")
	public String appliedModelList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/applied_model_list/data")
	@ResponseBody
	public Object appliedModelListData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap
			.add(new Object[][] {
				{"partNo", paramMap.get("pPartNo")},
				{"productType", "20"}
			});

		RfcResponse rfcResponse = partsInfoService.getAppliedModelList(rfcParamMap);

		return rfcResponse.getTable("T_MODEL");
	}

	@RequestMapping("/popup/alternative_list")
	public String alternativeList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/alternative_list/data")
	@ResponseBody
	public Object alternativeListData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.put("partNo", paramMap.get("pPartNo"));//PartsNo

		RfcResponse rfcResponse = partsInfoService.getAlternativeList(rfcParamMap);

		return rfcResponse.getTable("T_MODEL");
	}
}
