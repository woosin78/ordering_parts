package org.jwebppy.portal.iv.eu.parts.order.invoice.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.order.OrderGeneralController;
import org.jwebppy.portal.iv.eu.parts.order.invoice.service.EuInvoiceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/order/invoice")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class EuInvoiceStatusController extends OrderGeneralController
{
	@Autowired
	private EuInvoiceStatusService invoiceStatusService;

	@RequestMapping("/invoice_status_list")
	public String list(Model model, WebRequest webRequest)
	{
		String sDate = CmDateFormatUtils.today().substring(0,7)+"-01";
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", sDate);
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/invoice_status/data")
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
		rfcParamMap.put("poNo", CmStringUtils.trimToEmpty(paramMap.get("pPoNo")));//P.O No.
		rfcParamMap.put("type", CmStringUtils.trimToEmpty(paramMap.get("pType")));//Type
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));
		rfcParamMap.put("invoiceNo", CmStringUtils.trimToEmpty(paramMap.get("pInvoiceNo")));//Invoice No.
		rfcParamMap.put("partsNo", CmStringUtils.trimToEmpty(paramMap.get("pPartsNo")));//PartsNo
		rfcParamMap.put("orderNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderNo")));//OrderNo

		RfcResponse rfcResponse = invoiceStatusService.getList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		FormatBuilder.with(dataList)
			.decimalFormat("NETWR","#,##0.00")
			.decimalFormat("BRGEW","#,##0.000")
			.dateFormat("ERDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			.dateFormat("FKDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			;

		return dataList;
	}

	@RequestMapping("/invoice_status_popup_list")
	public String invoiceStatusPopupList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/invoice_status_popup/data")
	@ResponseBody
	public Object invoiceStatusPopupData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("poNo", paramMap.get("pPoNo"));//P.O No.
		rfcParamMap.put("type", paramMap.get("pType"));//Type
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));
		rfcParamMap.put("invoiceNo", paramMap.get("pInvoiceNo"));//Invoice No.
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));//PartsNo
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));//OrderNo

		RfcResponse rfcResponse = invoiceStatusService.getList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		FormatBuilder.with(dataList)
			.decimalFormat("NETWR","#,##0.00")
			.decimalFormat("BRGEW","#,##0.000")
			.dateFormat("ERDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			.dateFormat("FKDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			;

		return dataList;
	}
}
