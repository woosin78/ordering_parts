package org.jwebppy.portal.iv.common.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.web.PortalGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/portal/iv/download/grid")
public class GridExcelDownloadController extends PortalGeneralController
{
	@PostMapping("/excel")
	public void excel(WebRequest webRequest, HttpServletResponse httpServletResponse) throws JsonParseException, JsonMappingException, IOException
	{
		String fileName = CmStringUtils.defaultString(webRequest.getParameter("fileName"), CmDateFormatUtils.now()) + ".xlsx";
		String data = webRequest.getParameter("data");

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, List<Object>> jsonMap = objectMapper.readValue(data, HashMap.class);

		List<Object> headerList = jsonMap.get("header");

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");

		Row headerRow = sheet.createRow(0);
		for (int i=0, size=headerList.size(); i<size; i++)
		{
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(CmStringUtils.trimToEmpty(headerList.get(i)));
		}

		List<Object> bodyList = jsonMap.get("body");

		for (int i=0, size=bodyList.size(); i<size; i++)
		{
			List<Object> contentList = (List<Object>)bodyList.get(i);

			Row contentRow = sheet.createRow(i+1);

			for (int j=0, size2=contentList.size(); j<size2; j++)
			{
				Cell contentCell = contentRow.createCell(j);
				contentCell.setCellValue(CmStringUtils.trimToEmpty(contentList.get(j)));
			}
		}

		httpServletResponse.setContentType("application/download;charset=utf-8");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary");
		httpServletResponse.setHeader("Set-Cookie", "fileDownload=true; path=/");//ajax $.filedownload 의 event (successCallback 등) 이 동작히기 위해 필요

		workbook.write(httpServletResponse.getOutputStream());

		workbook.close();
		workbook = null;
	}
}

