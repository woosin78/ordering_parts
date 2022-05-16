package org.jwebppy.portal.iv.eu.parts.domestic.order.display.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.order.EuOrderGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.order.display.service.EuOrderDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH + "/order/display")
public class EuFileDownloadController extends EuOrderGeneralController
{
	@Autowired
	private EuOrderDisplayService orderDisplayService;

	@RequestMapping("/pdf_download")
	public void download(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		DataList dataList = orderDisplayService.getDownload(rfcParamMap);

		String fileName = getFileName(rfcParamMap.getString("mode"), rfcParamMap.getString("orderNo"));
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

	private String getFileName(String mode, String orderNo)
	{
		String fileName = "";

		if ("BT06".equals(mode))
		{
			fileName = orderNo + "_Quotation.pdf";
		}
		else if ("BT07".equals(mode))
		{
			fileName = orderNo + "_ProformaInvoice.pdf";
		}
		else if("BT08".equals(mode))
		{
			fileName = orderNo + "_Inquiry.pdf";
		}
		else if("Y006".equals(mode))
		{
			fileName = orderNo + "_Order.pdf";
		}

		return fileName;
	}
}
