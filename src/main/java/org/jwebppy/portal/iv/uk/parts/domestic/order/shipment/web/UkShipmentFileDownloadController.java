package org.jwebppy.portal.iv.uk.parts.domestic.order.shipment.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.order.UkOrderGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.order.shipment.service.UkShipmentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/order/shipment")
public class UkShipmentFileDownloadController extends UkOrderGeneralController
{
	@Autowired
	private UkShipmentStatusService shipmentStatusService;

	@RequestMapping("/pdf_download")
	public void download(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("shipmentNo", paramMap.get("pShipmentNo"));

		DataList dataList = shipmentStatusService.getDownload(rfcParamMap);

		String fileName = "PackingList.pdf";
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