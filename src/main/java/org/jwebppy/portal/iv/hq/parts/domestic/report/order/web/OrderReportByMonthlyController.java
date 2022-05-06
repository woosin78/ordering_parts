package org.jwebppy.portal.iv.hq.parts.domestic.report.order.web;

import java.util.Map;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.report.order.service.OrderReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/report/order/monthly")
public class OrderReportByMonthlyController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderReportService orderReportService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		String thisYear = CmDateFormatUtils.format(CmDateTimeUtils.now(), "yyyy");

		model.addAttribute("pYear", Integer.parseInt(CmStringUtils.defaultIfEmpty(webRequest.getParameter("pYear"), thisYear)));
		model.addAttribute("thisYear", Integer.parseInt(thisYear));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap
			.add(new Object[][] {
				{"year", CmStringUtils.defaultIfEmpty(paramMap.get("pYear"), CmDateFormatUtils.format(CmDateTimeUtils.now(), "yyyy"))},
				{"query", "Z_ZSS_M001_Q004_D"}
			});

		return orderReportService.getList(rfcParamMap);
	}
}
