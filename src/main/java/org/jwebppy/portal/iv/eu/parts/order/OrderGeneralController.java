package org.jwebppy.portal.iv.eu.parts.order;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.parts.common.web.EuPartsGeneralController;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOrderItemDto;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuSimulationResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class OrderGeneralController extends EuPartsGeneralController
{
	@Autowired
	private Environment environment;

	protected String getOrderUploadFilePath()
	{
		File path = new File(environment.getProperty("file.upload.rootPath") + File.separator + "order");

		if (!path.exists())
		{
			path.mkdir();
		}

		return path.getAbsolutePath();
	}

	protected List<EuOrderItemDto> getItemsFromUploadedFile(@RequestParam("file") MultipartFile multipartFile)
	{
		try
		{
			String contentType = multipartFile.getContentType();

			if (isExcelFile(contentType))
			{
				if (isXlsxFormat(contentType))
				{
					return getOrderItemsInXlsxFormat(multipartFile.getInputStream());
				}
				else
				{
					return getOrderItemsInXlsFormat(multipartFile.getInputStream());
				}
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	protected List<EuOrderItemDto> getOrderItemsFromExcelFile(String fileName, InputStream inputStream)
	{
		String extension = FilenameUtils.getExtension(fileName);

		if (CmStringUtils.equalsIgnoreCase(extension, "xls"))
		{
			return getOrderItemsInXlsFormat(inputStream);
		}

		return getOrderItemsInXlsxFormat(inputStream);
	}

	private List<EuOrderItemDto> getOrderItemsInXlsFormat(InputStream inputStream)
	{
		HSSFWorkbook hssfWorkbook = null;
		List<EuOrderItemDto> orderItems = new LinkedList<>();

		try
		{
			hssfWorkbook = new HSSFWorkbook(inputStream);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			for (int i=0, size=hssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				EuOrderItemDto orderItem = new EuOrderItemDto();
				orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
				orderItem.setMaterialNo(getValueFromExcel(hssfSheet.getRow(i).getCell(0)));
				
				String sQty = getValueFromExcel(hssfSheet.getRow(i).getCell(1));
				Integer qty = 0;
				try{
					Double dQty = (Double.parseDouble(sQty.trim()));
					qty = dQty.intValue();
				} catch (Exception e){
					qty = 0;
				}
				orderItem.setOrderQty(qty.toString());

				orderItems.add(orderItem);
			}

			hssfWorkbook.close();
			hssfWorkbook = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (hssfWorkbook != null)
			{
				try
				{
					hssfWorkbook.close();
					hssfWorkbook = null;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return orderItems;
	}

	private List<EuOrderItemDto> getOrderItemsInXlsxFormat(InputStream inputStream)
	{
		XSSFWorkbook xssfWorkbook = null;
		List<EuOrderItemDto> orderItems = new LinkedList<>();

		try
		{
			xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

			for (int i=0, size=xssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				EuOrderItemDto orderItem = new EuOrderItemDto();
				orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
				orderItem.setMaterialNo(getValueFromExcel(xssfSheet.getRow(i).getCell(0)));
				
				String sQty = getValueFromExcel(xssfSheet.getRow(i).getCell(1));
				Integer qty = 0;
//				try{
					Double dQty = (Double.parseDouble(sQty.trim()));
					qty = dQty.intValue();
//				} catch (Exception e){
//					qty = 0;
//				}
				orderItem.setOrderQty(qty.toString());

				orderItems.add(orderItem);
			}

			xssfWorkbook.close();
			xssfWorkbook = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (xssfWorkbook != null)
			{
				try
				{
					xssfWorkbook.close();
					xssfWorkbook = null;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return orderItems;
	}

	private String getValueFromExcel(Object cell)
	{
		if (cell == null)
		{
			return null;
		}

		String value = null;

		if (cell instanceof XSSFCell)
		{
			XSSFCell xssfCell = (XSSFCell)cell;

			if (xssfCell.getCellType() == CellType.NUMERIC)
			{
				value = ((Double)xssfCell.getNumericCellValue()).toString();
			}
			else
			{
				value = CmStringUtils.trimToEmpty(xssfCell.getStringCellValue());
			}
		}
		else
		{
			HSSFCell hssfCell = (HSSFCell)cell;

			if (hssfCell.getCellType() == CellType.NUMERIC)
			{
				value = ((Double)hssfCell.getNumericCellValue()).toString();
			}
			else
			{
				value = CmStringUtils.trimToEmpty(hssfCell.getStringCellValue());
			}
		}

		if (CmStringUtils.endsWith(value, ".0"))
		{
			return CmStringUtils.removeEnd(value, ".0");
		}
		else
		{
			return value;
		}
	}

	protected boolean isExcelFile(String contentType)
	{
		if (CmStringUtils.equals(contentType, "application/vnd.ms-excel") || CmStringUtils.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		{
			return true;
		}
		return false;
	}

	protected boolean isXlsxFormat(String contentType)
	{
		if (CmStringUtils.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		{
			return true;
		}

		return false;
	}

	protected void makeOrderItemForm(EuSimulationResultDto simulationResult)
	{
		int DEFAULT_ROW_COUNT = 20;

		List<EuOrderItemDto> normalOrderItems = simulationResult.getNormalOrderItems();
		int size = normalOrderItems.size();
		int lineNo = 1;

		if (size > 0)
		{
			EuOrderItemDto orderItem = normalOrderItems.get(size-1);
			lineNo = CmNumberUtils.toInt(orderItem.getLineNo(), 10) / 10 + 1;
		}

		for (int i=0, count=DEFAULT_ROW_COUNT-lineNo; i<=count; i++)
		{
			EuOrderItemDto orderItem = new EuOrderItemDto();
			orderItem.setLineNo(CmStringUtils.leftPad(lineNo*10, 6, "0"));

			normalOrderItems.add(orderItem);

			lineNo++;
		}
	}
}
