package org.jwebppy.portal.iv.hq.parts.export.report.sales.web;

import java.util.Map;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.report.sales.service.ExSalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/report/sales/monthly")
public class ExSalesReportByMonthlyController extends PartsExportGeneralController
{
	@Autowired
	private ExSalesReportService salesReportService;

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

		return salesReportService.getList(rfcParamMap);
	}
}
