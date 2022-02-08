package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.hq.common.HqCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/create")
public class OrderCreateController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderCreateService orderCreateService;

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		model.addAttribute("docType", CmStringUtils.defaultIfEmpty(webRequest.getParameter("docType"), "C"));
		model.addAttribute("poNo", CmStringUtils.stripStart(rfcParamMap.getCustomerNo(), "0") + CmDateFormatUtils.format(CmDateTimeUtils.now(), PortalCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/write2")
	public String write2(Model model, WebRequest webRequest)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		model.addAttribute("docType", CmStringUtils.defaultIfEmpty(webRequest.getParameter("docType"), "C"));
		model.addAttribute("poNo", CmStringUtils.stripStart(rfcParamMap.getCustomerNo(), "0") + CmDateFormatUtils.format(CmDateTimeUtils.now(), PortalCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/header_info")
	@ResponseBody
	public Object headerInfo(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		return orderCreateService.getHeaderInfo(rfcParamMap);
	}

	@RequestMapping("/order_type/data")
	@ResponseBody
	public Object orderTypeData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		return orderCreateService.getOrderType(rfcParamMap);
	}

	@PostMapping("/valid_check")
	@ResponseBody
	public Object validCheck(@ModelAttribute OrderDto order)
	{
		String[] materialNos = CmStringUtils.split(order.getMaterialNo(), HqCommonVo.DELIMITER);

		List<OrderItemDto> orderItems = new LinkedList<>();

		for (int i=0, length=materialNos.length; i<length; i++)
		{
			if (CmStringUtils.isNotEmpty(materialNos[i]))
			{
				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
				orderItem.setMaterialNo(materialNos[i]);

				orderItems.add(orderItem);
			}
		}

		order.setOrderItems(orderItems);

		return orderCreateService.checkValidPartsToOrder(order);
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute OrderDto order)
	{
		if (CmStringUtils.isNotEmpty(order.getMaterialNo()))
		{
			PartsErpDataMap userInfoMap = getErpUserInfo();

			order.setCorp(userInfoMap.getCorpNo());
			order.setUsername(userInfoMap.getUsername());
			order.setLanguage(userInfoMap.getLang());
			order.setSalesOrg(userInfoMap.getSalesOrg());
			order.setDistChannel(userInfoMap.getDistChannel());
			order.setDivision(userInfoMap.getDivision());
			order.setSoldToNo(userInfoMap.getCustomerNo());
			order.setSoldToName(userInfoMap.getCustomerName());

			List<OrderItemDto> orderItems = new LinkedList<>();

			String[] lineNos = CmStringUtils.splitPreserveAllTokens(order.getLineNo(), HqCommonVo.DELIMITER);
			String[] materialNos = CmStringUtils.splitPreserveAllTokens(order.getMaterialNo(), HqCommonVo.DELIMITER);
			String[] orderQtyies = CmStringUtils.splitPreserveAllTokens(order.getOrderQty(), HqCommonVo.DELIMITER);
			String[] lotQties = CmStringUtils.splitPreserveAllTokens(order.getLotQty(), HqCommonVo.DELIMITER);
			String[] uoms = CmStringUtils.splitPreserveAllTokens(order.getUom(), HqCommonVo.DELIMITER);
			String[] availabilities = CmStringUtils.splitPreserveAllTokens(order.getAvailability(), HqCommonVo.DELIMITER);

			for (int i=0, length=materialNos.length; i<length; i++)
			{
				if (CmStringUtils.isNotEmpty(materialNos[i]))
				{
					OrderItemDto orderItem = new OrderItemDto();

					orderItem.setLineNo(lineNos[i]);
					orderItem.setMaterialNo(materialNos[i]);
					orderItem.setOrderQty(orderQtyies[i]);
					orderItem.setLotQty(lotQties[i]);
					orderItem.setUom(uoms[i]);
					orderItem.setAvailability(availabilities[i]);

					orderItems.add(orderItem);
				}
			}

			order.setOrderItems(orderItems);

			RfcResponse rfcResponse = orderCreateService.save(order);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("orderNo", rfcResponse.getString("LV_VENLR"));
			resultMap.put("errorMsg", rfcResponse.getString("E_MEG"));

			return resultMap;
		}

		return null;
	}

	@RequestMapping("/popup/ship_to_party")
	public Object shipToParty(@RequestParam Map<String, Object> paramMap)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/ship_to_party/data")
	@ResponseBody
	public Object shipToPartyData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"customerNo", "pCustomerNo"},
				{"customerName", "pCustomerName"}
			});

		return orderCreateService.getShipToParty(rfcParamMap);
	}

	@RequestMapping("/popup/shipping_info")
	public Object shippingInfo(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/shipping_info/data")
	@ResponseBody
	public Object shippingInfoData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.putAll(paramMap);
		rfcParamMap.put("customerNo", rfcParamMap.getCustomerNo());

		return orderCreateService.getShippingInfo(rfcParamMap);
	}

	@RequestMapping("/transfer_to_order")
	public Object transferOrder(@RequestParam Map<String, Object> paramMap)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/transfer_to_order/save")
	@ResponseBody
	public Object saveTransferOrder(@RequestParam Map<String, Object> paramMap)
	{
		DataMap rfcParamMap = new DataMap(paramMap);

		RfcResponse rfcResponse = orderCreateService.transferOrder(rfcParamMap);
		DataList dataList = rfcResponse.getTable("RETURN");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("TYPE", "S");
		resultMap.put("SALESDOCUMENT_EX", rfcResponse.getString("SALESDOCUMENT_EX"));

		if (!dataList.isEmpty())
		{
			DataMap dataMap = new DataMap(dataList.get(0));

			resultMap.put("TYPE", dataMap.getString("TYPE"));
			resultMap.put("MESSAGE", dataMap.getString("MESSAGE"));
		}

		return resultMap;
	}

	@RequestMapping("/popup/error_report")
	public String errorReport()
	{
		return DEFAULT_VIEW_URL;
	}
}
