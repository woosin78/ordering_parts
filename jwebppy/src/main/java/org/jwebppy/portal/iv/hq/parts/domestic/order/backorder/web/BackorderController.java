package org.jwebppy.portal.iv.hq.parts.domestic.order.backorder.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.backorder.service.BackorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsCommonVo.DOMESTIC_REQUEST_PATH + "/order/backorder")
public class BackorderController extends PartsDomesticGeneralController
{
	@Autowired
	private BackorderService backorderService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateMonth(CmDateTimeUtils.now().minusMonths(6))));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));//Order No.
		rfcParamMap.put("orderType", paramMap.get("pOrderType"));//Order Type
		rfcParamMap.put("poNo", paramMap.get("pPoNo"));//P.O. No.
		rfcParamMap.put("orderPartNo", paramMap.get("pOrderPartNo"));//Order Part No.
		rfcParamMap.putDate("fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateMonth(CmDateTimeUtils.now().minusMonths(6))));
		rfcParamMap.putDate("toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today()));

		RfcResponse rfcResponse = backorderService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_BACKORDER_LIST");

		FormatBuilder.with(dataList)
			.dateFormat(new String[] { "AUDAT", "PRETD" })
			.qtyFormat(new String[] { "KWMENG_SO", "KWMENG_SH", "KWMENG" })
			.decimalFormat("NETWR");

		return dataList;
	}
}
