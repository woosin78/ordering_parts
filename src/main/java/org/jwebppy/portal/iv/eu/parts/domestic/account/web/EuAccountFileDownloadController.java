package org.jwebppy.portal.iv.eu.parts.domestic.account.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.account.service.EuAccountStatementService;
import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/account/download")
public class EuAccountFileDownloadController extends PartsGeneralController
{
	/*
	 EXCEL

		I_BGTYP P
		I_BUDAT 20200220
		I_USERID P_EUFL01

		Z_EP_STATEMENT_ACCOUNT_REPORT		ZSS_PARA_DIV_EP_STATE_ACC_REP
	*/

	@Autowired
	private EuAccountStatementService accountStatementService;

	@RequestMapping("/excel_download")
	public void excelDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse) throws JsonParseException, JsonMappingException, IOException
	{
		//DataMap rfcParamMap = new DataMap(getUserInfo());
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("pDate", CmStringUtils.trimToEmpty(paramMap.get("pDate")).replaceAll("-", ""));

		String fileName = "StatementOfAccount.xlsx";
//		String data = webRequest.getParameter("data");
//		DataList dataList = accountStatementService.getExcelDownload(rfcParamMap);

		RfcResponse rfcResponse = accountStatementService.getExcelDownload(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_OUTTAB");

//		ObjectMapper objectMapper =new ObjectMapper();
//		Map<String, List<Object>> jsonMap = objectMapper.readValue(data, HashMap.class);
//		List<Object> headerList = jsonMap.get("header");
//		Invoice Date	Invoice No.	Due Date	Before Due	Over Due(1~60 Days)	Over Due(61~180 Days)	Over Due(Over 181~)

		List<Object> headerList = new ArrayList<>();
		headerList.add(0, "Invoice Date");
		headerList.add(1, "Invoice No.");
		headerList.add(2, "Due Date");
		headerList.add(3, "Before Due");
		headerList.add(4, "Over Due(1~60 Days)");
		headerList.add(5, "Over Due(61~180 Days)");
		headerList.add(6, "Over Due(Over 181~)");
		headerList.add(7, "");

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");

		Row headerRow = sheet.createRow(0);
		for (int i=0, size=headerList.size(); i<size; i++)
		{
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(CmStringUtils.trimToEmpty(headerList.get(i)));
		}

/*
Invoice Date
Invoice No.
Due Date
Before Due
Over Due(1~60 Days)
Over Due(61~180 Days)
Over Due(Over 181~)
나머지

0 BSTKD
1 BUDAT
2 BUKRS
3 DAY00
4 DAY180
5 DAY60
6 DAYEND
7 DMBTR
8 GSBER
9 GTEXT
10 INVNO
11 KUNNR
12 VBELN_DN
13 VBELN_SH
14 VBELN_SH_AR
15 WAERS
16 WDAY00
17 WDAY180
18 WDAY60
19 WDAYEND
20 WRBTR
21 ZEXDT
*/
		FormatBuilder.with(dataList)
		.dateFormat(new String[] {"BUDAT","ZEXDT"}, EuCommonVo.DEFAULT_DATE_FORMAT);

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			Row contentRow = sheet.createRow(i+1);
			DataMap dataMap = (DataMap)dataList.get(i);

			for (int j=0, size2=headerList.size(); j<size2; j++)
			{
				Cell contentCell = contentRow.createCell(j);

				if(j==0) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("BUDAT")));
				}
				if(j==1) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("INVNO")));
				}
				if(j==2) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("ZEXDT")));
				}
				if(j==3) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("DAY00")));
				}
				if(j==4) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("DAY60")));
				}
				if(j==5) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("DAY180")));
				}
				if(j==6) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("DAYEND")));
				}
				if(j==7) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("DMBTR")));
				}
			}
		}

		httpServletResponse.setContentType("application/download;charset=utf-8");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary");

		workbook.write(httpServletResponse.getOutputStream());

		workbook.close();
		workbook = null;
	}

/*
 	PDF Download

	I_BGTYP P
	I_BUDAT 20200221
	I_USERID P_EUFL01
*/
	@RequestMapping("/pdf_download")
	public void pdfDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("pDate", CmStringUtils.trimToEmpty(paramMap.get("pDate")).replaceAll("-", ""));

		DataList dataList = accountStatementService.getPdfDownload(rfcParamMap);

		String fileName = "StatementOfAccount.pdf";
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
