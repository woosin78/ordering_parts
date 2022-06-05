package org.jwebppy.portal.iv.uk.parts.domestic.order.invoice.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.order.UkOrderGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.order.invoice.service.UkInvoiceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/order/invoice")
public class UkInvoiceFileDownloadController extends UkOrderGeneralController
{
	@Autowired
	private UkInvoiceStatusService invoiceStatusService;

	@RequestMapping("/pdf_download")
	public void download(@RequestParam Map<String, Object> paramMap
						,@RequestParam(value="item_chk", required=true) ArrayList<String> itemChks
						,HttpServletResponse httpServletResponse)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("chkCount", paramMap.get("chkCount"));
		DataList dataList = invoiceStatusService.getPdfDownload(rfcParamMap, itemChks);

		String now = CmDateFormatUtils.now(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);
		String fileName = "InvoicStatus_"+now+".pdf";
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