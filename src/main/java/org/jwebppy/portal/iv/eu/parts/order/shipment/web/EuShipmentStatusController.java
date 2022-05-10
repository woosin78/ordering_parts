package org.jwebppy.portal.iv.eu.parts.order.shipment.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.order.OrderGeneralController;
import org.jwebppy.portal.iv.eu.parts.order.shipment.service.EuShipmentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/order/shipment")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class EuShipmentStatusController extends OrderGeneralController
{
	@Autowired
	private EuShipmentStatusService shipmentStatusService;

	@RequestMapping("/shipment_status_list")
	public String list(Model model, WebRequest webRequest)
	{
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", CmDateFormatUtils.theFirstDateThisMonth());
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/shipment_status/data")
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
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("partsNo", CmStringUtils.trimToEmpty(paramMap.get("pPartsNo")));//PartsNo
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));
		rfcParamMap.put("orderNo", CmStringUtils.trimToEmpty(paramMap.get("pOrderNo")));//OrderNo

		RfcResponse rfcResponse = shipmentStatusService.getList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_HEAD");

		FormatBuilder.with(dataList)
			.decimalFormat("BRGEW","#,##0.000")
			.dateFormat("DTABF", EuCommonVo.DEFAULT_DATE_FORMAT)
			.decimalFormat("COUNT", "#");

		return dataList;
	}

	@RequestMapping("/shipment_status_detail")
	public Object detail(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/shipment_status_detail/data")
	@ResponseBody
	public Object detailData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("shipmentNo", CmStringUtils.trimToEmpty(paramMap.get("pShipmentNo")));
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));

		RfcResponse rfcResponse = shipmentStatusService.getDetail(rfcParamMap);
		DataList mainHeadResult = rfcResponse.getTable("LT_HEAD");
		DataList itemList = rfcResponse.getTable("LT_DETAIL");

		FormatBuilder.with(mainHeadResult);
		FormatBuilder.with(itemList)
			.decimalFormat("NETPR","#,##0.00")
			.decimalFormat("NETWR","#,##0.00")
			.decimalFormat("LFIMG", "#");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("mainHeadResult", mainHeadResult);
		resultMap.put("item", itemList);

		return resultMap;
	}
}
