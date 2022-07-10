package org.jwebppy.portal.iv.uk.parts.domestic.complaint.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.common.web.UkPartsDomesticGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.complaint.service.UkComplaintDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/complaint/display")
public class UkComplaintDisplayFileDownloadController extends UkPartsDomesticGeneralController
{
	@Autowired
	private UkComplaintDisplayService complaintDisplayService;

	@RequestMapping("/pdf_download")
	public void pdfDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));
		rfcParamMap.put("docType", paramMap.get("pDocType"));

		RfcResponse rfcResponse = complaintDisplayService.getDetail(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_FILE2");
		String fileName = "complaint.pdf";
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

				bufferOutputStream.write((byte[])dataMap.get("FILE_DATA"));
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

	@RequestMapping("/file_download")
	public void fileDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("fileName", paramMap.get("fileName"));
		rfcParamMap.put("orderNo", paramMap.get("orderNo"));
		rfcParamMap.put("intNo", paramMap.get("intNo"));
		rfcParamMap.put("docuItem", paramMap.get("docuItem"));

		RfcResponse rfcResponse = complaintDisplayService.getFileDown(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_FILE");
		//String fileName = paramMap.get("fileName").toString()+".pdf";
		String fileName = paramMap.get("fileName").toString();
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

				bufferOutputStream.write((byte[])dataMap.get("FILE_DATA"));
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