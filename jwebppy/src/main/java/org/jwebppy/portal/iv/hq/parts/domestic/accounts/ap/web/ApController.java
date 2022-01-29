package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ap.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.accounts.ap.service.ApService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsCommonVo.DOMESTIC_REQUEST_PATH + "/accounts/ap")
public class ApController extends PartsDomesticGeneralController
{
	@Autowired
	private ApService apService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.addDate(new Object[][] {
			{"fromDate", CmDateFormatUtils.theFirstDateMonth(CmDateTimeUtils.now())},
			{"toDate", CmDateFormatUtils.today()}
		});

		RfcResponse rfcResponse = apService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_LIST");
		Map summaryMap = (Map)rfcResponse.getResultMap().get("LS_9188");

		reCalculatePriceByCurrency(dataList, new String[] { "TLAMT", "RETAMT", "CHAMT", "TOTAL", "ARREAR" }, null, null, 100);
		reCalculatePriceByCurrency(dataList, new String[] { "TOTAL" }, null, null, -1);
		reCalculatePriceByCurrency(summaryMap, new String[] { "CREDAMT", "DUEAMT", "CURAMT", "PROAMT", "BALAMT" }, null, null, 100);

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] { "TLAMT", "RETAMT", "CHAMT", "TOTAL", "ARREAR" })
			.dateFormat(new String[] { "CRDAT", "ENDAT" });

		FormatBuilder.with(summaryMap)
			.decimalFormat(new String[] { "CREDAMT", "DUEAMT", "CURAMT", "PROAMT", "BALAMT" });

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("summary", summaryMap);
		resultMap.put("list", dataList);

		return resultMap;
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewDate(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.add("shipmentNo", CmStringUtils.leftPad((String)paramMap.get("pShipmentNo"), 10, "0"))
			.addByKey(new Object[][] {
				{"fromInvoiceNo", "pFromInvoiceNo"},
				{"toInvoiceNo", "pToInvoiceNo"},
				{"partNo", "pPartNo"},
				{"shipmentNo", "pShipmentNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"}
			});

		RfcResponse rfcResponse = apService.getView(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_INVOICE_DETAIL");

		reCalculatePriceByCurrency(dataList, new String[] { "NETPR", "NETWR", "MWSBP" }, null, null, 100);

		FormatBuilder.with(dataList)
			.integerFormat("VEPOS")
			.deleteFrontZero(new String[] {"TKNUM2", "EXIDV"})
			.decimalFormat(new String[] {"NETPR", "NETWR", "MWSBP"})
			.dateFormat("FKDAT");

		return dataList;
	}
}
