package org.jwebppy.portal.iv.hq.parts.domestic.order.display.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.display.service.OrderDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsCommonVo.DOMESTIC_REQUEST_PATH + "/order/display")
public class InquiryDisplayController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderDisplayService orderDisplayService;

	@RequestMapping("/inquiry_list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/inquiry_list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderType", CmStringUtils.trimToEmpty(paramMap.get("pOrderType")));//Order Type
		rfcParamMap.put("orderNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderNo")));//Order No.
		rfcParamMap.put("poNo", CmStringUtils.trimToEmpty(paramMap.get("pPoNo")));//Purchase Order No.
		rfcParamMap.put("orderPartNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderPartNo")));//Order Part No.
		rfcParamMap.put("status", CmStringUtils.trimToEmpty(paramMap.get("pStatus")));//Status
		rfcParamMap.putDate("fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		rfcParamMap.putDate("toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today()));
		rfcParamMap.put("docType", CmStringUtils.defaultString(paramMap.get("pDocType"), "A"));

		RfcResponse rfcResponse = orderDisplayService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		FormatBuilder.with(dataList)
			.decimalFormat("T_NETWR")
			.qtyFormat("COUNT")
			.dateFormat("ERDAT")
			.dateFormat("ERZET", CmDateFormatUtils.getTimeFormat());

		return dataList;
	}

	@RequestMapping("/inquiry_view")
	public Object view(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/inquiry_view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
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
			.qtyFormat("REQ_QTY");

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
