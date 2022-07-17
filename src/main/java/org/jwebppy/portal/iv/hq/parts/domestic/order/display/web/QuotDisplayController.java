package org.jwebppy.portal.iv.hq.parts.domestic.order.display.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.display.service.OrderDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/display/quot")
public class QuotDisplayController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderDisplayService orderDisplayService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"orderType", "pOrderType"},
				{"orderNo", "pOrderNo"},
				{"poNo", "pPoNo"},
				{"orderPartNo", "pOrderPartNo"},
				{"status", "pStatus"},
				{"docType", "pDocType"}
			})
			.addDate(new Object[][] {
				{"fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth())},
				{"toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today())}
			});

		RfcResponse rfcResponse = orderDisplayService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		FormatBuilder.with(dataList)
			.decimalFormat("T_NETWR")
			.qtyFormat("COUNT")
			.dateFormat("ERDAT")
			.dateFormat("ERZET", CmDateFormatUtils.getTimeFormat());

		return dataList;
	}

	@RequestMapping({"/view", "/popup/view"})
	public Object view(@RequestParam Map<String, Object> paramMap, Model model, ServletWebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return webRequest.getRequest().getRequestURI();
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.with(paramMap)
			.addByKey("orderNo", "orderNo")
			.add("docType", "B");

		RfcResponse rfcResponse = orderDisplayService.getView(rfcParamMap);

		DataMap generalMap = rfcResponse.getStructure("LS_GENERAL");
		DataMap mainHeadResultMap = rfcResponse.getStructure("LS_MAIN_HEAD_RESULT");
		DataList itemList = rfcResponse.getTable("LT_ITEM");

		FormatBuilder.with(generalMap).dateFormat("VDATU");
		FormatBuilder.with(mainHeadResultMap).decimalFormat(new String[] {"NETWR", "CREDIT", "TOTAL_WEIGHT"});
		FormatBuilder.with(itemList)
			.decimalFormat(new String[] {"KBETR", "NET_PRICE", "NET_VALUE"})
			.qtyFormat("REQ_QTY");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("remark", rfcResponse.getString("LV_REMARK"));
		resultMap.put("nettx", rfcResponse.getString("P_NETTX"));
		resultMap.put("first", rfcResponse.getStructure("LS_FIRST"));
		resultMap.put("generalZterm", rfcResponse.getTable("LT_GERNERAL_ZTERM"));
		resultMap.put("general", generalMap);
		resultMap.put("mainHeadResult", mainHeadResultMap);
		resultMap.put("item", itemList);

		return resultMap;
	}
}
