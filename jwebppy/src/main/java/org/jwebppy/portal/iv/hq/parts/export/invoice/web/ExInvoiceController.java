package org.jwebppy.portal.iv.hq.parts.export.invoice.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.invoice.service.ExInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/invoice")
public class ExInvoiceController extends PartsExportGeneralController
{
	@Autowired
	private ExInvoiceService invoiceService;

	private static final Map<String, String> fileNameMap = new HashMap<>();

	static {
		fileNameMap.put("CI", "Invoice.pdf");
		fileNameMap.put("PL", "PackingList.pdf");
		fileNameMap.put("DL", "PartsList.pdf");
		fileNameMap.put("EDI", "Invoice_Detail_List.txt");
	}

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
				{"invoiceNo", "pInvoiceNo"},
				{"poNo", "pPoNo"},
				{"orderPartNo", "pOrderPartNo"},
			})
			.addDate(new Object[][] {
				{"fromDate", CmStringUtils.defaultIfEmpty(paramMap.get("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth())},
				{"toDate", CmStringUtils.defaultIfEmpty(paramMap.get("pToDate"), CmDateFormatUtils.today())}
			});

		RfcResponse rfcResponse = invoiceService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_DETAIL");

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] {"NETWR", "VOLUM", "BRGEW", "T_NETWR"})
			.qtyFormat(new String[] {"LFIMG", "COUNT1"})
			.dateFormat(new String[] {"ZFOBDT", "ZFETA"});

		return dataList;
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
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
			.addByKey(new Object[][] {
				{"invoiceNo", "invoiceNo"},
				{"poNo", "pPoNo"},
				{"orderPartNo", "pOrderPartNo"}
			});

		RfcResponse rfcResponse = invoiceService.getView(rfcParamMap);

		DataList dataList = rfcResponse.getTable("T_DETAIL");

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] {"NETPR", "NETWR", "NTGEW", "BRGEW"})
			.deleteFrontZero("TKNUM")
			.dateFormat("ZFOBDT");

		return dataList;
	}

	@RequestMapping("/download/{mode}")
	public void download(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String mode, String[] invoiceNo, String[] shipmentNo)
	{
		if ("BL".equals(mode))
		{
			PartsErpDataMap rfcParamMap = getErpUserInfo();

			rfcParamMap.add(new Object[][] {
				{"mode", mode},
				{"invoiceNo", invoiceNo[0]}
			});

			RfcResponse rfcResponse = invoiceService.getBlDownload(rfcParamMap);

			DataList dataList = rfcResponse.getTable("T_DATA");

			if (CollectionUtils.isNotEmpty(dataList))
			{
				downloadByRfc(response, dataList, "FILE_DATA", CmStringUtils.trimToEmpty(((Map)dataList.get(0)).get("FILE_NAME")));
			}
		}
		else
		{
			PartsErpDataMap rfcParamMap = getErpUserInfo();

			rfcParamMap.with(paramMap)
				.add(new Object[][] {
					{"mode", mode},
					{"invoiceNo", invoiceNo},
					{"shipmentNo", shipmentNo}
				})
				.addDateByKey(new Object[][] {
					{"fromDate", "fromDate"},
					{"toDate", "toDate"}
				});

			RfcResponse rfcResponse = invoiceService.getDownload(rfcParamMap);

			if (rfcResponse != null)
			{
				DataList dataList = ("EDI".equals(mode)) ? rfcResponse.getTable("LT_TAB") : rfcResponse.getTable("T_PDF");

				downloadByRfc(response, dataList, "LINE", getFileName(mode));
			}
		}
	}

	private String getFileName(String mode)
	{
		return fileNameMap.get(mode);
	}
}
