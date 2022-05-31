package org.jwebppy.portal.iv.uk.parts.domestic.order.create.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.order.UkOrderGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderItemDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkSimulationResultDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.service.UkOrderSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/order/create")
@PreAuthorize("!hasRole('ROLE_DP_EUDO_PARTS_READ_ONLY_DEALER')")
public class UkOrderSimulationController extends UkOrderGeneralController
{
	@Autowired
	private UkOrderSimulationService orderSimulationService;

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
	public Object simulation(@ModelAttribute UkOrderDto order)
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

			List<UkOrderItemDto> orderItems = new LinkedList<>();

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				UkOrderItemDto orderItem = new UkOrderItemDto();
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
				List<UkOrderItemDto> orderItems = new LinkedList<>();

				String[] materialNos = order.getMaterialNo().split("\\^");
				String[] orderQtyies = order.getOrderQty().split("\\^");

				for (int i=0, length=materialNos.length; i<length; i++)
				{
					if (CmStringUtils.isNotEmpty(materialNos[i]))
					{
						UkOrderItemDto orderItem = new UkOrderItemDto();
						orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
						orderItem.setMaterialNo(materialNos[i]);
						orderItem.setOrderQty(orderQtyies[i]);

						orderItems.add(orderItem);
					}
				}

				order.setOrderItems(orderItems);
			}
		}

		UkSimulationResultDto simulationResult = new UkSimulationResultDto();

		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			simulationResult = orderSimulationService.simulation(order);
		}

		makeOrderItemForm(simulationResult);

		return simulationResult;
	}
}
