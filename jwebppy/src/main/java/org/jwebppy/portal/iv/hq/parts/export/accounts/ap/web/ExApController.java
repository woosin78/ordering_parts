package org.jwebppy.portal.iv.hq.parts.export.accounts.ap.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.accounts.ap.service.ExApService;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/accounts/ap")
public class ExApController extends PartsExportGeneralController
{
	@Autowired
	private ExApService apService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.plusMonths(-6)));
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
				{"fromOrderNo", "pFromOrderNo"},
				{"toOrderNo", "pToOrderNo"},
				{"fromInvoiceNo", "pFromInvoiceNo"},
				{"toInvoiceNo", "pToInvoiceNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"}
			});

		DataList dataList = apService.getList(rfcParamMap).getTable("T_INVOICE1");

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] {"WRBTR", "WRSHB", "WRBTR2"})
			.dateFormat(new String[] {"BLDAT", "FAEDT"})
			.integerFormat("VERZN");

		return dataList;
	}
}
