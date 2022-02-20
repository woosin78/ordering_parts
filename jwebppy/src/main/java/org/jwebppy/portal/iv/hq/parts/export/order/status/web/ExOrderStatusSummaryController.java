package org.jwebppy.portal.iv.hq.parts.export.order.status.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.order.status.service.ExOrderStatusSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/order/status/summary")
public class ExOrderStatusSummaryController extends PartsExportGeneralController
{
	@Autowired
	private ExOrderStatusSummaryService orderStatusSummaryService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));
		model.addAttribute("pStatus", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pStatus"), "0"));

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
				{"orderNo", "pOrderNo"},
				{"poNo", "pPoNo"},
				{"orderPartNo", "pOrderPartNo"},
				{"status", "pStatus"}
			})
			.addDate(new Object[][] {
				{"fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth())},
				{"toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today())}
			});

		RfcResponse rfcResponse = orderStatusSummaryService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_DETAIL");

		FormatBuilder.with(dataList)
			.qtyFormat(new String[] {"MENGE", "LFIMG_DL"})
			.dateFormat("ZFOBDT", "AUDAT");

		return dataList;
	}
}
