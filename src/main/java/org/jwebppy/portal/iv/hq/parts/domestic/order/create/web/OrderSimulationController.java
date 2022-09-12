package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
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
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.SimulationResultDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderCreateService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderSimulationService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.util.OrderCreationUtils;
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
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/create")
public class OrderSimulationController extends PartsDomesticGeneralController
{
	private final String[] AVAILABLE_EXCEL_FORMAT = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
	private final int DEFAULT_ROW_COUNT = 10;

    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Value("${file.upload.order}")
    private String orderPath;

    private String uploadPath;

    @Autowired
    private OrderCreateService orderCreateService;

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
	private OrderSimulationService orderSimulationService;

	public Object validation(OrderDto order)
	{
		if (CmStringUtils.isNotEmpty(order.getMaterialNo()))
		{
			List<OrderItemDto> orderItems = new LinkedList<>();

			String[] materialNos = order.getMaterialNo().split("\\" + PortalCommonVo.DELIMITER);
			String[] orderQties = order.getOrderQty().split("\\" + PortalCommonVo.DELIMITER);

			for (int i=0, length=materialNos.length; i<length; i++)
			{
				if (CmStringUtils.isNotEmpty(materialNos[i]))
				{
					OrderItemDto orderItem = new OrderItemDto();
					orderItem.setLineNo(OrderCreationUtils.lineNo(i+1));
					orderItem.setMaterialNo(materialNos[i]);

					try
					{
						orderItem.setOrderQty(orderQties[i]);
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						//orderItem.setOrderQty("0");
					}

					orderItems.add(orderItem);
				}
			}

			ErpDataMap userInfoMap = getErpUserInfo();

			order.setOrderItems(orderItems);
			order.setUsername(userInfoMap.getUsername());
			order.setLanguage(userInfoMap.getLangForSap());

			DataList dataList = orderSimulationService.getLotQties(order);
			List<OrderItemDto> normalOrderItems = new ArrayList<>();

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(dataMap.getString("ITEM"));
				orderItem.setMaterialNo(CmStringUtils.defaultIfEmpty(dataMap.getString("MATERIAL_ENT"), dataMap.getString("MATERIAL")));

				int qty = (int)dataMap.getDouble("QTY", 0);

				if (qty > 0)
				{
					orderItem.setOrderQty(Integer.toString(qty));
				}

				if (dataMap.isNotEmptyValue("MATERIAL_TEXT"))
				{
					orderItem.setDescription(dataMap.getString("MATERIAL_TEXT"));

					int lotQty = (int)dataMap.getDouble("LOT_QTY", 0);
					lotQty = (lotQty == 0) ? 1 : lotQty;

					orderItem.setLotQty(Integer.toString(lotQty));
				}

				normalOrderItems.add(orderItem);
			}

			//Sales Lot 오류 자재 필터링
			if (CmStringUtils.equals(order.getFgFilteringInvalidSalesLotItem(), IvCommonVo.YES))
			{
				order.setOrderItems(normalOrderItems);
				order.filteringInvalidSalesLotOrderItems();
			}

			SimulationResultDto simulationResult = new SimulationResultDto();
			simulationResult.setNormalOrderItems(normalOrderItems);

			makeOrderItemForm(simulationResult);

			return simulationResult;
		}

		return EMPTY_RETURN_VALUE;
	}

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
	public Object simulation(@ModelAttribute OrderDto order)
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

				String[] materialNos = order.getMaterialNo().split("\\" + PortalCommonVo.DELIMITER);
				String[] orderQties = order.getOrderQty().split("\\" + PortalCommonVo.DELIMITER);

				if (materialNos.length != orderQties.length)
				{
					return validation(order);
				}

				for (int i=0, length=materialNos.length; i<length; i++)
				{
					if (CmStringUtils.isNotEmpty(materialNos[i]))
					{
						OrderItemDto orderItem = new OrderItemDto();
						orderItem.setLineNo(OrderCreationUtils.lineNo(i+1));
						orderItem.setMaterialNo(materialNos[i]);
						orderItem.setOrderQty(orderQties[i]);

						orderItems.add(orderItem);
					}
				}

				order.setOrderItems(orderItems);
			}
		}

		order.setUsername(userInfoMap.getUsername());
		order.setLanguage(userInfoMap.getLangForSap());

		//중복 입력된 자재 필터링
		if (CmStringUtils.equals(order.getFgFilteringDuplicateItem(), IvCommonVo.YES))
		{
			order.filteringDuplicateItems();
		}

		//Sales Lot 오류 자재 필터링
		if (CmStringUtils.equals(order.getFgFilteringInvalidSalesLotItem(), IvCommonVo.YES))
		{
			orderSimulationService.setLotQty(order);
			order.filteringInvalidSalesLotOrderItems();
		}

		SimulationResultDto simulationResult = new SimulationResultDto();

		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			simulationResult = orderSimulationService.simulation(order);
		}
		else
		{
			if (ObjectUtils.isNotEmpty(order.getOhhSeq()))
			{
				OrderHistoryHeaderDto orderHistoryHeader = orderCreateService.getOrderHistory(order.getOhhSeq());

				if (ObjectUtils.isNotEmpty(orderHistoryHeader))
				{
					List<OrderItemDto> orderItems = new LinkedList<>();
					int index = 0;

					for (OrderHistoryItemDto orderHistoryItem: ListUtils.emptyIfNull(orderHistoryHeader.getOrderHistoryItems()))
					{
						OrderItemDto orderItem = new OrderItemDto();
						orderItem.setLineNo(OrderCreationUtils.lineNo(index+1));
						orderItem.setMaterialNo(orderHistoryItem.getMaterialNo());
						orderItem.setOrderQty(orderHistoryItem.getOrderQty());

						orderItems.add(orderItem);
					}

					simulationResult.setNormalOrderItems(orderItems);
				}
			}

			//시뮬레이션 결과 정상 건이 없을 경우 사용자 정보에서 Credit 을 가져와 넣어준다.
			ErpDataMap paramMap = getErpUserInfo()
					.add(new Object[][] {
						{"orderType", order.getOrderType()},
						{"priceGroup", order.getPriceGroup()},
						{"docType", order.getDocType()}
					});

			DataMap dataMap = orderCreateService.getHeaderInfo(paramMap);

			simulationResult.setCredit(Formatter.getDefDecimalFormat(dataMap.getString("CREDIT")));
			simulationResult.setCreditCurrency(dataMap.getString("WAERS"));
		}

		//중복 자재 입력 오류는 시뮬레이션 실행 전에 필터링해 둔 것을 넣어준다.
		simulationResult.getErrorOrderItems().addAll(ListUtils.emptyIfNull(order.getDuplicateOrderItems()));

		//Sales Lot 오류 건과 정상 건을 합친다
		simulationResult.setNormalOrderItems(ListUtils.union(ListUtils.emptyIfNull(order.getInvalidSalesLotOrderItems()), simulationResult.getNormalOrderItems()));

		makeOrderItemForm(simulationResult);

		return simulationResult;
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
				orderItem.setLineNo(OrderCreationUtils.lineNo(i));
				orderItem.setMaterialNo(getValueFromExcel(hssfSheet.getRow(i).getCell(0)));
				orderItem.setOrderQty(getValueFromExcel(hssfSheet.getRow(i).getCell(1)));

				orderItems.add(orderItem);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
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

			try
			{
				inputStream.close();
				inputStream = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
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
				String materialNo = getValueFromExcel(xssfSheet.getRow(i).getCell(0));
				String orderQty = getValueFromExcel(xssfSheet.getRow(i).getCell(1));

				if (CmStringUtils.isEmpty(materialNo) || CmStringUtils.isEmpty(orderQty))
				{
					continue;
				}

				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(OrderCreationUtils.lineNo(i+1));
				orderItem.setMaterialNo(materialNo);
				orderItem.setOrderQty(orderQty);

				orderItems.add(orderItem);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
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

			try
			{
				inputStream.close();
				inputStream = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
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
		if (CmArrayUtils.contains(AVAILABLE_EXCEL_FORMAT, contentType))
		{
			return true;
		}
		return false;
	}

	protected boolean isXlsxFormat(String contentType)
	{
		if (CmStringUtils.equals(contentType, AVAILABLE_EXCEL_FORMAT[1]))
		{
			return true;
		}

		return false;
	}

	protected void makeOrderItemForm(SimulationResultDto simulationResult)
	{
		List<OrderItemDto> normalOrderItems = simulationResult.getNormalOrderItems();
		int size = normalOrderItems.size();
		int lineNo = 1;

		if (size > 0)
		{
			OrderItemDto orderItem = normalOrderItems.get(size-1);

			String beginLineNo = (orderItem.isInvalidLotQty()) ? "0": orderItem.getLineNo();
			lineNo = CmNumberUtils.toInt(beginLineNo, 10) / 10 + 1;
		}

		int rowCount = (size / DEFAULT_ROW_COUNT + 1) * DEFAULT_ROW_COUNT;

		for (int i=0, count=rowCount-size; i<count; i++)
		{
			OrderItemDto orderItem = new OrderItemDto();
			orderItem.setLineNo(OrderCreationUtils.lineNo(lineNo));

			normalOrderItems.add(orderItem);

			lineNo++;
		}
	}
}
