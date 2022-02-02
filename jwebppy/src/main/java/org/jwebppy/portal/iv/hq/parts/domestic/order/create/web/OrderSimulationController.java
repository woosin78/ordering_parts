package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.SimulationResultDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/create")
public class OrderSimulationController extends PartsDomesticGeneralController
{
	@Autowired
	private Environment environment;

	@Autowired
	private OrderSimulationService orderSimulationService;

	@PostMapping("/simulation/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile)
	{
		if (isExcelFile(multipartFile.getContentType()))
		{
			try
			{
				String fileName = getUsername() + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

				multipartFile.transferTo(new File(getOrderUploadFilePath() + File.separator + fileName));

				return fileName;
			}
			catch (IllegalStateException | IOException e)
			{
				e.printStackTrace();
			}
		}

		return EMPTY_RETURN_VALUE;
	}

	@PostMapping("/simulation")
	@ResponseBody
	public Object simulation(@ModelAttribute OrderDto order)
	{
		ErpDataMap userInfoMap = getErpUserInfo();

		order.setUsername(userInfoMap.getUsername());
		order.setLanguage(userInfoMap.getLang());

		if (CmStringUtils.isNotEmpty(order.getFileName()))//파일업로드로 시뮬레이션 실행
		{
			File file = null;

			try
			{
				file = new File(getOrderUploadFilePath() + File.separator + order.getFileName());

				if (file.exists())
				{
					order.setOrderItems(getOrderItemsFromExcelFile(file.getName(), new FileInputStream(file)));
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		else if (CmStringUtils.equals(order.getSimulationFrom(), "GPES"))//GPES 쇼핑카드로 시뮬레이션 실행
		{
			RfcResponse rfcResponse = orderSimulationService.getItemsFromGpes(userInfoMap);

			DataList dataList = rfcResponse.getTable("T_SHOPPING_CART");

			List<OrderItemDto> orderItems = new LinkedList<>();

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setMaterialNo(dataMap.getString("MATNR"));
				orderItem.setOrderQty(dataMap.getString("QTY", "1"));
				orderItem.setUom(dataMap.getString("MEINS"));

				orderItems.add(orderItem);
			}

			order.setOrderItems(orderItems);
		}
		else
		{
			if (CmStringUtils.isNotEmpty(order.getMaterialNo()))
			{
				List<OrderItemDto> orderItems = new LinkedList<>();

				String[] materialNos = order.getMaterialNo().split("\\^");
				String[] orderQtyies = order.getOrderQty().split("\\^");

				for (int i=0, length=materialNos.length; i<length; i++)
				{
					if (CmStringUtils.isNotEmpty(materialNos[i]))
					{
						OrderItemDto orderItem = new OrderItemDto();
						orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
						orderItem.setMaterialNo(materialNos[i]);
						orderItem.setOrderQty(orderQtyies[i]);

						orderItems.add(orderItem);
					}
				}

				order.setOrderItems(orderItems);
			}
		}

		SimulationResultDto simulationResult = new SimulationResultDto();

		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			simulationResult = orderSimulationService.simulation(order);
		}

		makeOrderItemForm(simulationResult);

		return simulationResult;
	}

	protected String getOrderUploadFilePath()
	{
		File path = new File(environment.getProperty("file.upload.rootPath") + File.separator + "order");

		if (!path.exists())
		{
			path.mkdir();
		}

		return path.getAbsolutePath();
	}

	protected List<OrderItemDto> getItemsFromUploadedFile(@RequestParam("file") MultipartFile multipartFile)
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

	protected List<OrderItemDto> getOrderItemsFromExcelFile(String fileName, InputStream inputStream)
	{
		String extension = FilenameUtils.getExtension(fileName);

		if (CmStringUtils.equalsIgnoreCase(extension, "xls"))
		{
			return getOrderItemsInXlsFormat(inputStream);
		}

		return getOrderItemsInXlsxFormat(inputStream);
	}

	private List<OrderItemDto> getOrderItemsInXlsFormat(InputStream inputStream)
	{
		HSSFWorkbook hssfWorkbook = null;
		List<OrderItemDto> orderItems = new LinkedList<>();

		try
		{
			hssfWorkbook = new HSSFWorkbook(inputStream);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			for (int i=0, size=hssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
				orderItem.setMaterialNo(getValueFromExcel(hssfSheet.getRow(i).getCell(0)));
				orderItem.setOrderQty(getValueFromExcel(hssfSheet.getRow(i).getCell(1)));

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

	private List<OrderItemDto> getOrderItemsInXlsxFormat(InputStream inputStream)
	{
		XSSFWorkbook xssfWorkbook = null;
		List<OrderItemDto> orderItems = new LinkedList<>();

		try
		{
			xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

			for (int i=0, size=xssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
				orderItem.setMaterialNo(getValueFromExcel(xssfSheet.getRow(i).getCell(0)));
				orderItem.setOrderQty(getValueFromExcel(xssfSheet.getRow(i).getCell(1)));

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

	protected void makeOrderItemForm(SimulationResultDto simulationResult)
	{
		int DEFAULT_ROW_COUNT = 20;

		List<OrderItemDto> normalOrderItems = simulationResult.getNormalOrderItems();
		int size = normalOrderItems.size();
		int lineNo = 1;

		if (size > 0)
		{
			OrderItemDto orderItem = normalOrderItems.get(size-1);
			lineNo = CmNumberUtils.toInt(orderItem.getLineNo(), 10) / 10 + 1;
		}

		for (int i=0, count=DEFAULT_ROW_COUNT-lineNo; i<=count; i++)
		{
			OrderItemDto orderItem = new OrderItemDto();
			orderItem.setLineNo(CmStringUtils.leftPad(lineNo*10, 6, "0"));

			normalOrderItems.add(orderItem);

			lineNo++;
		}
	}
}
