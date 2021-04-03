package org.jwebppy.portal.dbkr.scm.parts.domestic.order.display.web;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.dbkr.DbkrCommonVo;
import org.jwebppy.portal.dbkr.scm.parts.PartsGeneralController;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.display.service.OrderDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/dbkr/scm/parts/domestic/order/display")
public class OrderDisplayController extends PartsGeneralController
{
	@Autowired
	private OrderDisplayService orderDisplayService;

	@RequestMapping("/order_list")
	public String orderList(Model model, WebRequest webRequest)
	{
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", CmDateFormatUtils.theFirstDateThisMonth());
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		model.addAttribute("erpUserInfo", getErpUserInfo());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/order_list/data")
	@ResponseBody
	public Object orderData(@RequestParam Map<String, Object> paramMap)
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
		rfcParamMap.put("orderType", paramMap.get("pOrderType"));//Order Type
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));//Order No.
		rfcParamMap.put("poNo", paramMap.get("pPoNo"));//Purchase Order No.
		rfcParamMap.put("orderPartNo", paramMap.get("pOrderPartNo"));//Order Part No.
		rfcParamMap.put("status", paramMap.get("pStatus"));//Status
		rfcParamMap.put("fromDate", CmStringUtils.defaultString(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.defaultString(paramMap.get("pToDate")).replaceAll("-", ""));
		rfcParamMap.put("docType", CmStringUtils.defaultString(paramMap.get("pDocType"), "C"));

		RfcResponse rfcResponse = orderDisplayService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("LT_SEARCH");

		FormatBuilder.with(dataList)
			.dateFormat("ERDAT")
			.dateFormat("ERZET", DbkrCommonVo.DEFAULT_TIME_MMHH_FORMAT);

		return dataList;
	}

	@RequestMapping("/order_detail")
	public Object orderDetail(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		model.addAttribute("erpUserInfo", getErpUserInfo());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/order_detail_popup")
	public Object orderDetailPopup(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		model.addAttribute("erpUserInfo", getErpUserInfo());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/order_detail/data")
	@ResponseBody
	public Object orderDetailData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("orderNo"));
		rfcParamMap.put("docType", paramMap.get("pDocType"));

		RfcResponse rfcResponse = orderDisplayService.getDetail(rfcParamMap);

		DataMap generalMap = rfcResponse.getStructure("LS_GENERAL");
		DataMap mainHeadResult = rfcResponse.getStructure("LS_MAIN_HEAD_RESULT");
		DataList itemList = rfcResponse.getTable("LT_ITEM");

		FormatBuilder.with(generalMap).dateFormat("VDATU");
		FormatBuilder.with(mainHeadResult).decimalFormat(new String[] {"NETWR", "CREDIT", "TOTAL_WEIGHT"});
		FormatBuilder.with(itemList)
			.decimalFormat(new String[] {"KBETR", "NET_PRICE", "NET_VALUE"})
			.decimalFormat("REQ_QTY", "#,###");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("remark", rfcResponse.getString("LV_REMARK"));
		resultMap.put("nettx", rfcResponse.getString("P_NETTX"));
		resultMap.put("first", rfcResponse.getStructure("LS_FIRST"));
		resultMap.put("general", generalMap);
		resultMap.put("mainHeadResult", mainHeadResult);
		resultMap.put("item", itemList);

		return resultMap;
	}

	@RequestMapping("/pdf_download")
	public void download(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse) throws FileNotFoundException
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		DataList dataList = orderDisplayService.getDownload(rfcParamMap);

		String fileName = null;
		String orderNo = rfcParamMap.getString("orderNo");

		switch (rfcParamMap.getString("mode"))
		{
			case "BT06": fileName = orderNo + "_Quotation.pdf";
			case "BT07": fileName = orderNo + "_ProformaInvoice.pdf";
			case "BT08": fileName = orderNo + "_Inquiry.pdf";
			case "Y006": fileName = orderNo + "_Order.pdf";
		}

		if (CmStringUtils.isEmpty(fileName))
		{
			throw new FileNotFoundException();
		}

		BufferedOutputStream bufferOutputStream = null;

		try
		{
			httpServletResponse.setContentType("application/octet-stream; charset=UTF-8");
			httpServletResponse.setHeader("Content-Disposition","attachment; filename=" +  fileName + ";");
			httpServletResponse.setHeader("Content-Transfer", "binary");

			bufferOutputStream = new BufferedOutputStream (httpServletResponse.getOutputStream());

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				bufferOutputStream.write((byte[])dataMap.get("LINE"));
			}

			bufferOutputStream.flush();
			bufferOutputStream.close();
			bufferOutputStream = null;
		}
		catch (IOException e)
		{
			try
			{
				if (bufferOutputStream != null)
				{
					bufferOutputStream.close();
					bufferOutputStream = null;
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
