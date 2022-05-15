package org.jwebppy.portal.iv.eu.parts.domestic.order.display.web;

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
import org.jwebppy.portal.iv.eu.parts.domestic.order.EuOrderGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.order.display.service.EuOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/order/display/order_status")
public class EuOrderStatusFileDownloadController extends EuOrderGeneralController
{
	@Autowired
	private EuOrderStatusService orderStatusService;


	@RequestMapping("/excel_download")
	public void excelDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse httpServletResponse) throws JsonParseException, JsonMappingException, IOException
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("detailVBELN", paramMap.get("detailVBELN"));//P.O No.

		String fileName = "Order_Status_Detail.xlsx";

		RfcResponse rfcResponse = orderStatusService.getTotalDownload(rfcParamMap);
		DataList dataList = rfcResponse.getTable("T_DETAIL");

		List<Object> headerList = new ArrayList<>();
		headerList.add(0, "Order No.");
		headerList.add(1, "P.O No.");
		headerList.add(2, "Order Date");
		headerList.add(3, "Item No");
		headerList.add(4, "Order Type");
		headerList.add(5, "Order Part No.");
		headerList.add(6, "Supply Part No");
		headerList.add(7, "Description");
		headerList.add(8, "Order Qty");
		headerList.add(9, "Reserved Qty");
		headerList.add(10, "Packed Qty");
		headerList.add(11, "Invoiced Qty");
		headerList.add(12, "S.Completion Qty");
		headerList.add(13, "B/O Qty");
		headerList.add(14, "First ETD");
		headerList.add(15, "Updated ETD");

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");

		Row headerRow = sheet.createRow(0);
		for (int i=0, size=headerList.size(); i<size; i++)
		{
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(CmStringUtils.trimToEmpty(headerList.get(i)));
		}

		FormatBuilder.with(dataList)
		.dateFormat(new String[] {"BUDAT","ZEXDT"}, EuCommonVo.DEFAULT_DATE_FORMAT)
		.dateFormat("AUDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
		.dateFormat("FST_ETD", EuCommonVo.DEFAULT_DATE_FORMAT)
		.dateFormat("LST_ETD", EuCommonVo.DEFAULT_DATE_FORMAT);

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			Row contentRow = sheet.createRow(i+1);
			DataMap dataMap = (DataMap)dataList.get(i);

			for (int j=0, size2=headerList.size(); j<size2; j++)
			{
				Cell contentCell = contentRow.createCell(j);

				if(j==0) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("VBELN")));
				}
				if(j==1) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("BSTKD")));
				}
				if(j==2) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("AUDAT")));
				}
				if(j==3) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("POSNR")));
				}
				if(j==4) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("KONDA_T")));
				}
				if(j==5) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("MATNR_SH")));
				}
				if(j==6) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("MATNR_SO")));
				}
				if(j==7) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("MAKTX")));
				}
				if(j==8) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("SH_QTY")));
				}
				if(j==9) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("RE_QTY")));
				}
				if(j==10) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("PI_QTY")));
				}
				if(j==11) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("IN_QTY")));
				}
				if(j==12) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("SO_QTY")));
				}
				if(j==13) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("RE_QTY")));
				}
				if(j==14) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("FST_ETD")));
				}
				if(j==15) {
					contentCell.setCellValue(CmStringUtils.trimToEmpty(dataMap.get("LST_ETD")));
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

}
