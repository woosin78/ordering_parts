package org.jwebppy.portal.iv.hq.parts.domestic.order.delivery.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.delivery.service.DeliveryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsCommonVo.DOMESTIC_REQUEST_PATH + "/order/delivery")
public class DeliveryStatusController extends PartsDomesticGeneralController
{
	@Autowired
	private DeliveryStatusService deliveryStatusService;

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
		rfcParamMap.putDate("fromDate", paramMap.get("pFromDate"));
		rfcParamMap.putDate("toDate", paramMap.get("pToDate"));
		rfcParamMap.putDate("fromOnboardDate", paramMap.get("pFromOnboardDate"));
		rfcParamMap.putDate("toOnboardDate", paramMap.get("pToOnboardDate"));
		rfcParamMap.put("shipmentNo", paramMap.get("pShipmentNo"));
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));
		rfcParamMap.put("partNo", paramMap.get("pPartsNo"));
		rfcParamMap.put("poNo", paramMap.get("pPoNo"));

		RfcResponse rfcResponse = deliveryStatusService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_LIST");

		//KRW, JPY 는 가격에 100을 곱해줌
		makePriceByCurrency(dataList, new String[] { "NETWR" }, "WAERK");

		FormatBuilder.with(dataList)
			.integerFormat("TKNUM")
			.qtyFormat(new String[] { "COUNT1", "COUNT2" })
			.decimalFormat(new String[] { "NETWR", "BRGEW" })
			.dateFormat("ZFOBDT");

		return dataList;
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
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putDate("fromDate", paramMap.get("pFromDate"));
		rfcParamMap.putDate("toDate", paramMap.get("pToDate"));
		rfcParamMap.putDate("fromOnboardDate", paramMap.get("pFromOnboardDate"));
		rfcParamMap.putDate("toOnboardDate", paramMap.get("pToOnboardDate"));
		rfcParamMap.put("shipmentNo", paramMap.get("pShipmentNo2"));
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));
		rfcParamMap.put("partsNo", paramMap.get("pPartsNo"));
		rfcParamMap.put("poNo", paramMap.get("pPoNo"));

		RfcResponse rfcResponse = deliveryStatusService.getView(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_DETAIL");

		//KRW, JPY 는 가격에 100을 곱해줌
		makePriceByCurrency(dataList, new String[] { "NETPR", "NETWR" }, "WAERK");

		FormatBuilder.with(dataList)
			.integerFormat(new String[] { "TKNUM", "EXIDV" })
			.qtyFormat("LFIMG")
			.decimalFormat(new String[] { "NETPR", "NETWR", "KWERT" })
			.dateFormat("ZFOBDT");

		return dataList;
	}
}
