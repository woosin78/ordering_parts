package org.jwebppy.portal.iv.hq.parts.domestic.order.status.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.status.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/status")
public class OrderStatusController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderStatusService orderStatusService;

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
				{"orderNo", "pOrderNo"},
				{"orderType", "pOrderType"},
				{"poNo", "pPoNo"},
				{"orderPartNo", "pOrderPartNo"}
			})
			.addDate(new Object[][] {
				{"fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth())},
				{"toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today())}
			});

		RfcResponse rfcResponse = orderStatusService.getList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_HEADER");

		FormatBuilder.with(dataList)
			.qtyFormat(new String[] { "SO_ITEM", "SO_QTY", "PI_QTY", "SH_QTY", "IN_QTY", "BO_AMT" })
			.dateFormat("ERDAT");

		return dataList;
	}

	@RequestMapping("/view")
	public Object view(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("orderNo"));

		RfcResponse rfcResponse = orderStatusService.getView(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_DETAIL");

		FormatBuilder.with(dataList)
			.dateFormat(new String[] { "AUDAT", "ERDAT", "FST_ETD", "LST_ETD" });

		return dataList;
	}
}
