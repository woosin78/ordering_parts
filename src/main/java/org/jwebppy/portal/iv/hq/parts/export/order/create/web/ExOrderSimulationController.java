package org.jwebppy.portal.iv.hq.parts.export.order.create.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
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
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderItemDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExSimulationResultDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.service.ExOrderSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/order/create")
public class ExOrderSimulationController extends PartsDomesticGeneralController
{
	private final int DEFAULT_ROW_COUNT = 10;

    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Value("${file.upload.order}")
    private String orderPath;

    private String uploadPath;

    @PostConstruct
    public void init()
    {
    	uploadPath = rootPath + File.separator + orderPath;

    	try
    	{
			FileUtils.forceMkdir(new File(uploadPath));
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }

	@Autowired
	private ExOrderSimulationService orderSimulationService;

	@PostMapping("/simulation/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile)
	{
		if (isExcelFile(multipartFile.getContentType()))
		{
			try
			{
				String fileName = getUsername() + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

				multipartFile.transferTo(new File(uploadPath + File.separator + fileName));

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
	public Object simulation(@ModelAttribute ExOrderDto order)
	{
		ErpDataMap userInfoMap = getErpUserInfo();

		if (CmStringUtils.isNotEmpty(order.getFileName()))//파일업로드로 시뮬레이션 실행
		{
			File file = null;

			try
			{
				file = new File(uploadPath + File.separator + order.getFileName());

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

			List<ExOrderItemDto> orderItems = new LinkedList<>();

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				ExOrderItemDto orderItem = new ExOrderItemDto();
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
				List<ExOrderItemDto> orderItems = new LinkedList<>();

				String[] materialNos = order.getMaterialNo().split("\\" + PortalCommonVo.DELIMITER);
				String[] orderQtyies = order.getOrderQty().split("\\" + PortalCommonVo.DELIMITER);

				for (int i=0, length=materialNos.length; i<length; i++)
				{
					if (CmStringUtils.isNotEmpty(materialNos[i]))
					{
						ExOrderItemDto orderItem = new ExOrderItemDto();
						orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
						orderItem.setMaterialNo(materialNos[i]);
						orderItem.setOrderQty(orderQtyies[i]);

						orderItems.add(orderItem);
					}
				}

				order.setOrderItems(orderItems);
			}
		}

		order.setUsername(userInfoMap.getUsername());
		order.setLanguage(userInfoMap.getLang());

		//중복 입력된 자재 필터링
		if (CmStringUtils.equals(order.getFgFilteringDuplicateItem(), IvCommonVo.YES))
		{
			order.filteringDuplicateItems();
		}

		//Sales Lot 오류 필터링
		if (CmStringUtils.equals(order.getFgFilteringInvalidSalesLotItem(), IvCommonVo.YES))
		{
			orderSimulationService.setSalesLot(order);
			order.filteringInvalidSalesLotOrderItems();
		}

		ExSimulationResultDto simulationResult = new ExSimulationResultDto();

		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			simulationResult = orderSimulationService.simulation(order);
		}

		//중복 자재 입력 오류는 시뮬레이션 실행 전에 필터링해 둔 것을 넣어준다.
		simulationResult.getErrorOrderItems().addAll(ListUtils.emptyIfNull(order.getDuplicateOrderItems()));

		//Sales Lot 오류 건과 정상 건을 합친다
		simulationResult.setNormalOrderItems(ListUtils.union(ListUtils.emptyIfNull(order.getInvalidSalesLotOrderItems()), simulationResult.getNormalOrderItems()));

		makeOrderItemForm(simulationResult);

		return simulationResult;
	}

	protected List<ExOrderItemDto> getItemsFromUploadedFile(@RequestParam("file") MultipartFile multipartFile)
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

	protected List<ExOrderItemDto> getOrderItemsFromExcelFile(String fileName, InputStream inputStream)
	{
		String extension = FilenameUtils.getExtension(fileName);

		if (CmStringUtils.equalsIgnoreCase(extension, "xls"))
		{
			return getOrderItemsInXlsFormat(inputStream);
		}

		return getOrderItemsInXlsxFormat(inputStream);
	}

	private List<ExOrderItemDto> getOrderItemsInXlsFormat(InputStream inputStream)
	{
		HSSFWorkbook hssfWorkbook = null;
		List<ExOrderItemDto> orderItems = new LinkedList<>();

		try
		{
			hssfWorkbook = new HSSFWorkbook(inputStream);
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

			for (int i=0, size=hssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				ExOrderItemDto orderItem = new ExOrderItemDto();
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

	private List<ExOrderItemDto> getOrderItemsInXlsxFormat(InputStream inputStream)
	{
		XSSFWorkbook xssfWorkbook = null;
		List<ExOrderItemDto> orderItems = new LinkedList<>();

		try
		{
			xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

			for (int i=0, size=xssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				ExOrderItemDto orderItem = new ExOrderItemDto();
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

	protected void makeOrderItemForm(ExSimulationResultDto simulationResult)
	{
		List<ExOrderItemDto> normalOrderItems = simulationResult.getNormalOrderItems();
		int size = normalOrderItems.size();
		int lineNo = 1;

		if (size > 0)
		{
			ExOrderItemDto orderItem = normalOrderItems.get(size-1);
			lineNo = CmNumberUtils.toInt(orderItem.getLineNo(), 10) / 10 + 1;
		}

		for (int i=0, count=DEFAULT_ROW_COUNT-lineNo; i<=count; i++)
		{
			ExOrderItemDto orderItem = new ExOrderItemDto();
			orderItem.setLineNo(CmStringUtils.leftPad(lineNo*10, 6, "0"));

			normalOrderItems.add(orderItem);

			lineNo++;
		}
	}
}
