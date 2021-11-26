package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.common.HqCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OnetimeAddressDto;
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
@RequestMapping(PartsCommonVo.DOMESTIC_REQUEST_PATH + "/order/create")
public class OrderCreateController extends PartsGeneralController
{
	@Autowired
	private OrderCreateService orderCreateService;

	@RequestMapping("/order_form")
	public String orderForm(Model model, WebRequest webRequest)
	{
		model.addAttribute("docType", CmStringUtils.defaultIfEmpty(webRequest.getParameter("docType"), "C"));
		model.addAttribute("erpUserInfo", getErpUserInfo());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/header_info")
	@ResponseBody
	public Object headerInfo(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap erpDateMap = getErpUserInfo();
		erpDateMap.put("AUART", paramMap.get("orderType"));
		erpDateMap.put("KONDA", paramMap.get("priceGroup"));
		erpDateMap.put("VBTYP", paramMap.get("docType"));

		return orderCreateService.getHeaderInfo(erpDateMap);
	}

	@RequestMapping("/order_type/data")
	@ResponseBody
	public Object orderTypeData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap erpDateMap = getErpUserInfo();
		erpDateMap.putAll(paramMap);

		return orderCreateService.getOrderType(erpDateMap);
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
			ErpDataMap userInfoMap = getErpUserInfo();

			order.setCorp(userInfoMap.getCorpNo());
			order.setUsername(userInfoMap.getUsername());
			order.setLanguage(userInfoMap.getLang());
			order.setSalesOrg(userInfoMap.getSalesOrg());
			order.setDistChannel(userInfoMap.getDistChannel());
			order.setDivision(userInfoMap.getDivision());

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

	@RequestMapping("/ship_to_party_list")
	public Object shipToPartyList(@RequestParam Map<String, Object> paramMap)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/ship_to_party_list/data")
	@ResponseBody
	public Object shipToPartyListData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap erpDateMap = getErpUserInfo();
		erpDateMap.put("customerNo", paramMap.get("pCustomerNo"));
		erpDateMap.put("customerName", paramMap.get("pCustomerName"));

		return orderCreateService.getShipToParty(erpDateMap);
	}

	@RequestMapping("/shipping_info_list")
	public Object shippingInfoList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/shipping_info_list/data")
	@ResponseBody
	public Object shippingInfoListData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap erpDateMap = getErpUserInfo();
		erpDateMap.put("orderType", paramMap.get("orderType"));
		erpDateMap.put("priceGroup", paramMap.get("priceGroup"));
		erpDateMap.put("customerNo", erpDateMap.getCustomerNo());

		return orderCreateService.getShippingInfo(erpDateMap);
	}

	@RequestMapping("/onetime_address_list")
	public Object onetimeAddressList(@RequestParam Map<String, Object> paramMap)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/onetime_address_list/data")
	@ResponseBody
	public Object onetimeAddressListData(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpDataMap = getErpUserInfo();

		onetimeAddress.setCorp(erpDataMap.getCorpName());
		onetimeAddress.setRegUsername(getUsername());

		return orderCreateService.getOnetimeAddresses(onetimeAddress);
	}

	@RequestMapping("/onetime_address/duplication_check")
	@ResponseBody
	public Object checkOnetimeAddressDuplication(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpDataMap = getErpUserInfo();

		onetimeAddress.setCorp(erpDataMap.getCorpName());
		onetimeAddress.setCustomerNo(CmStringUtils.stripStart(erpDataMap.getCustomerNo(), "0"));
		onetimeAddress.setRegUsername(getUsername());

		List<OnetimeAddressDto> onetimeAddresses = orderCreateService.getOnetimeAddressesForDuplicationCheck(onetimeAddress);

		if (CollectionUtils.isNotEmpty(onetimeAddresses))
		{
			return onetimeAddresses.get(0);
		}

		return null;
	}

	@RequestMapping("/onetime_address/save")
	@ResponseBody
	public Object saveOnetimeAddress(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpDataMap = getErpUserInfo();
		onetimeAddress.setCorp(erpDataMap.getCorpName());
		onetimeAddress.setCustomerNo(CmStringUtils.stripStart(erpDataMap.getCustomerNo(), "0"));
		onetimeAddress.setFgDelete(HqCommonVo.NO);

		return orderCreateService.saveOnetimeAddress(onetimeAddress);
	}

	@RequestMapping("/onetime_address/delete")
	@ResponseBody
	public Object deleteOnetimeAddress(@RequestParam("oaSeq") List<Integer> oaSeqs)
	{
		OnetimeAddressDto onetimeAddress = new OnetimeAddressDto();
		onetimeAddress.setCorp(getErpUserInfo().getCorpName());
		onetimeAddress.setRegUsername(getUsername());
		onetimeAddress.setFgDelete(HqCommonVo.YES);
		onetimeAddress.setOaSeqs(oaSeqs);

		return orderCreateService.deleteOnetimeAddress(onetimeAddress);
	}

	@RequestMapping("/onetime_address/postal_code")
	@ResponseBody
	public Object postalCode(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpDateMap = new ErpDataMap(getErpUserInfo());
		erpDateMap.put("country", onetimeAddress.getCountry());
		erpDateMap.put("postalCode", onetimeAddress.getPostalCode());

		return orderCreateService.getPostalCode(erpDateMap);
	}

	@RequestMapping("/onetime_address/tzone_list")
	@ResponseBody
	public Object tzone_list(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpDateMap = new ErpDataMap();
		erpDateMap.put("country", onetimeAddress.getCountry());
		erpDateMap.put("transportZone", onetimeAddress.getTransportZone());

		return orderCreateService.getTransportZone(erpDateMap);
	}

	@RequestMapping("/error_report")
	public String errorReport()
	{
		return DEFAULT_VIEW_URL;
	}
}
