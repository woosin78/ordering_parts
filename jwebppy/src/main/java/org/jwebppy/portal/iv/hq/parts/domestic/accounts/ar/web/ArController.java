package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ar.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.common.utils.PriceAdjustmentByCurrencyUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.accounts.ar.service.ArService;
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
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/accounts/ar")
public class ArController extends PartsDomesticGeneralController
{
	@Autowired
	private ArService arService;

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
				{"fromPoNo", "pFromPoNo"},
				{"toPoNo", "pToPoNo"},
				{"fromInvoiceNo", "pFromInvoiceNo"},
				{"toInvoiceNo", "pToInvoiceNo"},
				{"partNo", "pPartNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"}
			});

		RfcResponse rfcResponse = arService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_AP_INVOICE_DETAIL");

		PriceAdjustmentByCurrencyUtils.calcPriceByCurrency(dataList, new String[] {"NETPR", "WRBTR"}, "WAERS", new String[] {"KRW", "JPY"}, 100);

		FormatBuilder.with(dataList)
			.qtyFormat("MENGE")
			.decimalFormat(new String[] {"NETPR", "WRBTR"})
			.dateFormat("BLDAT");

		return dataList;
	}
}
