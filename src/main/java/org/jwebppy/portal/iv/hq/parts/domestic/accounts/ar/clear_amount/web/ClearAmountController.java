package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ar.clear_amount.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.accounts.ar.clear_amount.service.ClearAmountService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/accounts/ar/clear_amount")
public class ClearAmountController extends PartsDomesticGeneralController
{
	@Autowired
	private ClearAmountService clearAmountService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pYear", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pYear"), CmDateFormatUtils.year()));
		model.addAttribute("pMonth", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pMonth"), CmDateFormatUtils.month()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		String year = CmStringUtils.defaultIfEmpty(paramMap.get("pYear"), CmDateFormatUtils.year());
		String month = CmStringUtils.defaultIfEmpty(paramMap.get("pMonth"),  CmDateFormatUtils.month());
		String yearMonth = year + month;

		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap
			.add(new Object[][] {
				{"fgComplete", paramMap.get("pFgComplete")},
				{"yearMonth", yearMonth}
			});

		RfcResponse rfcResponse = clearAmountService.getList(rfcParamMap);

		DataMap dataMap = rfcResponse.getStructure("S_TOTAL");
		DataList dataList = rfcResponse.getTable("T_LIST");

		FormatBuilder.with(dataMap)
			.decimalFormat(new String[] {"S_AMOUNT", "TOT_AMOUNT", "T_AMOUNT"}, "#,###");

		FormatBuilder.with(dataList)
			.qtyFormat("QTY")
			.decimalFormat(new String[] {"SALES_AMT"})
			.dateFormat("SALE_DATE");

		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("SUMMARY", dataMap);
		resultMap.put("LIST", dataList);

		return resultMap;
	}
}
