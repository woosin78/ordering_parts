package org.jwebppy.portal.iv.eu.parts.domestic.order.backorder.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.web.EuPartsDomesticGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.order.backorder.service.EuBackorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH + "/order/backorder")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class EuBackorderController extends EuPartsDomesticGeneralController
{
	@Autowired
	private EuBackorderService backorderService;

	@RequestMapping("/backorder_list")
	public String list(Model model, WebRequest webRequest)
	{
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", CmDateFormatUtils.theFirstDateMonth(CmDateTimeUtils.now().minusMonths(6)));
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/backorder_list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		if (CmStringUtils.isEmpty(paramMap.get("pFromDate")))
		{
			paramMap.put("pFromDate", CmDateFormatUtils.theFirstDateMonth(CmDateTimeUtils.now().minusMonths(6)));
		}

		if (CmStringUtils.isEmpty(paramMap.get("pToDate")))
		{
			paramMap.put("pToDate", CmDateFormatUtils.today());
		}

		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderNo")));			//Order No.
		rfcParamMap.put("orderType", CmStringUtils.trimToEmpty(paramMap.get("pOrderType")));		//Order Type
		rfcParamMap.put("poNo", CmStringUtils.trimToEmpty(paramMap.get("pPoNo")));					//P.O. No.
		rfcParamMap.put("orderPartNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderPartNo")));	//Order Part No.
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));

		RfcResponse rfcResponse = backorderService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_BACKORDER_LIST");

		FormatBuilder.with(dataList)
			.dateFormat("AUDAT",EuCommonVo.DEFAULT_DATE_FORMAT)
			.dateFormat("PRETD",EuCommonVo.DEFAULT_DATE_FORMAT)
			.decimalFormat("NETWR", "#,##0.00");

		return dataList;
	}
}
