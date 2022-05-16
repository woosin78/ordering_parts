package org.jwebppy.portal.iv.uk.parts.domestic.order.display.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.uk.common.UkCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.order.UkOrderGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.order.display.service.UkOrderDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/order/display")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class UkQuotationDisplayController extends UkOrderGeneralController
{
	@Autowired
	private UkOrderDisplayService orderDisplayService;

	@RequestMapping("/quotation_list")
	public String quotationList(Model model, WebRequest webRequest)
	{
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", CmDateFormatUtils.theFirstDateThisMonth());
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/quotation_list/data")
	@ResponseBody
	public Object quotationData(@RequestParam Map<String, Object> paramMap)
	{
		if (CmStringUtils.isEmpty(paramMap.get("pFromDate")))
		{
			paramMap.put("pFromDate", CmDateFormatUtils.theFirstDateThisMonth());
		}

		if (CmStringUtils.isEmpty(paramMap.get("pToDate")))
		{
			paramMap.put("pToDate", CmDateFormatUtils.today());
		}

		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderType", CmStringUtils.trimToEmpty(paramMap.get("pOrderType")));//Order Type
		rfcParamMap.put("orderNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderNo")));//Order No.
		rfcParamMap.put("poNo", CmStringUtils.trimToEmpty(paramMap.get("pPoNo")));//Purchase Order No.
		rfcParamMap.put("orderPartNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderPartNo")));//Order Part No.
		rfcParamMap.put("status", CmStringUtils.trimToEmpty(paramMap.get("pStatus")));//Status
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));
		rfcParamMap.put("docType", CmStringUtils.defaultString(paramMap.get("pDocType"), "C"));

		RfcResponse rfcResponse = orderDisplayService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		FormatBuilder.with(dataList)
			.dateFormat("ERDAT")
			.dateFormat("ERZET", UkCommonVo.DEFAULT_TIME_MMHH_FORMAT);

		return dataList;
	}

	@RequestMapping("/quotation_detail")
	public Object quotationDetail(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/quotation_detail/data")
	@ResponseBody
	public Object quotationDetailData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("orderNo"));
		rfcParamMap.put("docType", paramMap.get("docType"));

		RfcResponse rfcResponse = orderDisplayService.getDetail(rfcParamMap);

		DataMap generalMap = rfcResponse.getStructure("LS_GENERAL");
		DataMap mainHeadResult = rfcResponse.getStructure("LS_MAIN_HEAD_RESULT");
		DataList itemList = rfcResponse.getTable("LT_ITEM");

		FormatBuilder.with(generalMap).dateFormat("VDATU");
		FormatBuilder.with(mainHeadResult).decimalFormat(new String[] {"NETWR", "CREDIT", "TOTAL_WEIGHT"});
		FormatBuilder.with(itemList)
			.decimalFormat(new String[] {"KBETR", "NET_PRICE", "NET_VALUE"})
			.decimalFormat("REQ_QTY", "#,###");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("remark", rfcResponse.getString("LV_REMARK"));
		resultMap.put("nettx", rfcResponse.getString("P_NETTX"));
		resultMap.put("first", rfcResponse.getStructure("LS_FIRST"));
		resultMap.put("general", generalMap);
		resultMap.put("mainHeadResult", mainHeadResult);
		resultMap.put("item", itemList);

		return resultMap;
	}
}
