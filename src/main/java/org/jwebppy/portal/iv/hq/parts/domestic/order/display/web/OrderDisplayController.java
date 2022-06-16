package org.jwebppy.portal.iv.hq.parts.domestic.order.display.web;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.display.service.OrderDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/display/order")
public class OrderDisplayController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderDisplayService orderDisplayService;

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
				{"orderType", "pOrderType"},
				{"orderNo", "pOrderNo"},
				{"poNo", "pPoNo"},
				{"orderPartNo", "pOrderPartNo"},
 				{"docType", "pDocType"}
			})
			.addDate(new Object[][] {
				{"fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth())},
				{"toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today())}
			});

		RfcResponse rfcResponse = orderDisplayService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		if (CollectionUtils.isNotEmpty(dataList))
		{
			double totalAmount = 0;
			int totalItemCount = 0;

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				totalAmount += dataMap.getDouble("T_NETWR");
				totalItemCount += dataMap.getInt("COUNT");
			}

			DataMap summaryMap = new DataMap();
			summaryMap.putAll((DataMap)dataList.get(0));
			summaryMap.clearValue("WAERK");
			summaryMap.put("T_NETWR", totalAmount);
			summaryMap.put("COUNT", totalItemCount);

			dataList.add(0, summaryMap);
		}

		FormatBuilder.with(dataList)
			.decimalFormat("T_NETWR")
			.qtyFormat("COUNT")
			.dateFormat("ERDAT")
			.dateFormat("ERZET", CmDateFormatUtils.getTimeFormat());

		return dataList;
	}

	@RequestMapping("/view")
	public Object view(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/popup/view")
	public Object viewPopup(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey("orderNo", "orderNo")
			.add("docType", "C");

		RfcResponse rfcResponse = orderDisplayService.getView(rfcParamMap);

		DataMap generalMap = rfcResponse.getStructure("LS_GENERAL");
		DataMap mainHeadResultMap = rfcResponse.getStructure("LS_MAIN_HEAD_RESULT");
		DataList itemList = rfcResponse.getTable("LT_ITEM");

		FormatBuilder.with(generalMap).dateFormat("VDATU");
		FormatBuilder.with(mainHeadResultMap)
			.decimalFormat(new String[] {"NETWR", "CREDIT"})
			.weightFormat("TOTAL_WEIGHT");
		FormatBuilder.with(itemList)
			.decimalFormat(new String[] {"KBETR", "NET_PRICE", "NET_VALUE"})
			.qtyFormat(new String[] {"REQ_QTY", "COM_QTY"});

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("remark", rfcResponse.getString("LV_REMARK"));
		resultMap.put("nettx", rfcResponse.getString("P_NETTX"));
		resultMap.put("first", rfcResponse.getStructure("LS_FIRST"));
		resultMap.put("generalZterm", rfcResponse.getTable("LT_GERNERAL_ZTERM"));
		resultMap.put("general", generalMap);
		resultMap.put("mainHeadResult", mainHeadResultMap);
		resultMap.put("item", itemList);

		return resultMap;
	}

	@RequestMapping("/download/confirm_sheet")
	public void downloadConfirmSheet(@RequestParam Map<String, Object> paramMap, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws FileNotFoundException
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		DataList dataList = orderDisplayService.getDownload(rfcParamMap);

		String fileName = null;
		String orderNo = rfcParamMap.getString("orderNo");
		String mode = rfcParamMap.getString("mode");

		if ("BT06".equals(mode))
		{
			fileName = "Quotation_" + orderNo + ".pdf";
		}
		else if ("BT07".equals(mode))
		{
			fileName = "ProformaInvoice_" + orderNo + ".pdf";
		}
		else if ("BT08".equals(mode))
		{
			fileName = "Inquiry_" + orderNo + ".pdf";
		}
		else if ("Y006".equals(mode))
		{
			fileName = "Order_" + orderNo + ".pdf";
		}

		if (CmStringUtils.isNotEmpty(fileName))
		{
			downloadByRfc(httpServletRequest, httpServletResponse, dataList, "LINE", fileName);
		}
	}
}
