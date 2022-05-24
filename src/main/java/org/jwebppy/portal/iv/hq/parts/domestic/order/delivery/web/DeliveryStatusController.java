package org.jwebppy.portal.iv.hq.parts.domestic.order.delivery.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.common.utils.PriceAdjustmentByCurrencyUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
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
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/delivery")
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

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"shipmentNo", "pShipmentNo"},
				{"orderNo", "pOrderNo"},
				{"partNo", "pOrderNo"},
				{"poNo", "pPoNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"},
				{"fromOnboardDate", "pFromOnboardDate"},
				{"toOnboardDate", "pToOnboardDate"}
			});

		RfcResponse rfcResponse = deliveryStatusService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_LIST");

		//KRW, JPY 는 가격에 100을 곱해줌
		PriceAdjustmentByCurrencyUtils.calcPriceByCurrency(dataList, new String[] {"NETWR"}, "WAERK", new String[] {"KRW", "JPY"}, 100);

		FormatBuilder.with(dataList)
			.decimalFormat("NETWR")
			.weightFormat("BRGEW")
			.qtyFormat(new String[] {"COUNT1", "COUNT2"})
			.integerFormat("TKNUM")
			.dateFormat("ZFOBDT");

		return dataList;
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		if (CmStringUtils.isNotEmpty(webRequest.getParameter("shipmentNo")))
		{
			model.addAttribute("pShipmentNo", webRequest.getParameter("shipmentNo"));
		}

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"shipmentNo", "pShipmentNo"},
				{"orderNo", "pOrderNo"},
				{"partsNo", "pPartsNo"},
				{"poNo", "pPoNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"},
				{"fromOnboardDate", "pFromOnboardDate"},
				{"toOnboardDate", "pToOnboardDate"}
			});

		RfcResponse rfcResponse = deliveryStatusService.getView(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_DETAIL");

		//KRW, JPY 는 가격에 100을 곱해줌
		PriceAdjustmentByCurrencyUtils.calcPriceByCurrency(dataList, new String[] {"NETPR", "NETWR"}, "WAERK", new String[] {"KRW", "JPY"}, 100);

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] {"NETPR", "NETWR", "KWERT"})
			.qtyFormat("LFIMG")
			.integerFormat(new String[] {"TKNUM", "EXIDV"})
			.dateFormat("ZFOBDT");

		return dataList;
	}
}
