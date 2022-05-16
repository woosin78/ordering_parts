package org.jwebppy.portal.iv.eu.parts.domestic.order.display.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.order.EuOrderGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.order.display.service.EuOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH + "/order/display")
public class EuOrderStatusController extends EuOrderGeneralController
{
	@Autowired
	private EuOrderStatusService orderStatusService;

	@RequestMapping("/order_status_list")
	public String list(Model model, WebRequest webRequest)
	{
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", CmDateFormatUtils.theFirstDateThisMonth());
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		ErpDataMap dataMap = getErpUserInfo();
		RfcResponse rfcResponse = orderStatusService.getMeaCustomerChk(dataMap);

		model.addAttribute("meaCustomerChk", rfcResponse.getString("O_CHECK1"));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/order_status/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
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
		rfcParamMap.put("orderNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderNo")));//Order No.
		rfcParamMap.put("orderType", CmStringUtils.trimToEmpty(paramMap.get("pOrderType")));//Order Type
		rfcParamMap.put("poNo", CmStringUtils.trimToEmpty(paramMap.get("pPoNo")));//P.O No.
		rfcParamMap.put("orderPartNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderPartNo")));//Order Part No.
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));

		RfcResponse rfcResponse = orderStatusService.getList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_HEADER");

		FormatBuilder.with(dataList)
			.dateFormat("ERDAT", EuCommonVo.DEFAULT_DATE_FORMAT);

		return dataList;
	}

	@RequestMapping("/order_status_detail")
	public Object detail(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/order_status_detail/data")
	@ResponseBody
	public Object detailData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("orderNo"));

		RfcResponse rfcResponse = orderStatusService.getDetail(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_DETAIL");

		FormatBuilder.with(dataList)
			.dateFormat("AUDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			.dateFormat("ERDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			.dateFormat("FST_ETD", EuCommonVo.DEFAULT_DATE_FORMAT)
			.dateFormat("LST_ETD", EuCommonVo.DEFAULT_DATE_FORMAT);

		return dataList;
	}
}
