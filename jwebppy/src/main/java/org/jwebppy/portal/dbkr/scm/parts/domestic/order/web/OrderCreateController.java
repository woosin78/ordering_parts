package org.jwebppy.portal.dbkr.scm.parts.domestic.order.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.dbkr.DbkrCommonVo;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.OrderGeneralController;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.OrderGeneralService;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OnetimeAddressDto;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OrderDto;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OrderItemDto;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.service.OrderCreateService;
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
public class OrderCreateController extends OrderGeneralController
{
	@Autowired
	private OrderCreateService orderCreateService;

	@Autowired
	private OrderGeneralService orderGeneralService;

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
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("AUART", paramMap.get("orderType"));
		rfcParamMap.put("KONDA", paramMap.get("priceGroup"));
		rfcParamMap.put("VBTYP", paramMap.get("docType"));

		return orderCreateService.getHeaderInfo(rfcParamMap);
	}

	@RequestMapping("/order_type/data")
	@ResponseBody
	public Object orderTypeData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		return orderGeneralService.getOrderType(rfcParamMap);
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute OrderDto order)
	{
		if (CmStringUtils.isNotEmpty(order.getMaterialNo()))
		{
			ErpDataMap userInfoMap = getErpUserInfo();

			order.setUsername(userInfoMap.getUsername());
			order.setLanguage(userInfoMap.getLang());
			order.setSalesOrg(userInfoMap.getSalesOrg());
			order.setDistChannel(userInfoMap.getDistChannel());
			order.setDivision(userInfoMap.getDivision());

			List<OrderItemDto> orderItems = new LinkedList<>();

			String[] lineNos = CmStringUtils.splitPreserveAllTokens(order.getLineNo(), "\\^");
			String[] materialNos = CmStringUtils.splitPreserveAllTokens(order.getMaterialNo(), "\\^");
			String[] orderQtyies = CmStringUtils.splitPreserveAllTokens(order.getOrderQty(), "\\^");
			String[] lotQties = CmStringUtils.splitPreserveAllTokens(order.getLotQty(), "\\^");
			String[] uoms = CmStringUtils.splitPreserveAllTokens(order.getUom(), "\\^");
			String[] availabilities = CmStringUtils.splitPreserveAllTokens(order.getAvailability(), "\\^");

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
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("customerNo", paramMap.get("pCustomerNo"));
		rfcParamMap.put("customerName", paramMap.get("pCustomerName"));

		return orderCreateService.getShipToParty(rfcParamMap);
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
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderType", paramMap.get("orderType"));
		rfcParamMap.put("priceGroup", paramMap.get("priceGroup"));
		rfcParamMap.put("customerNo", rfcParamMap.getCustomerNo());

		return orderCreateService.getShippingInfo(rfcParamMap);
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
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setRegUsername(getUsername());

		return orderCreateService.getOnetimeAddresses(onetimeAddress);
	}

	@RequestMapping("/onetime_address/duplication_check")
	@ResponseBody
	public Object checkOnetimeAddressDuplication(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setCustomerNo(CmStringUtils.stripStart(erpUserInfoMap.getCustomerNo(), "0"));
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
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setCustomerNo(CmStringUtils.stripStart(erpUserInfoMap.getCustomerNo(), "0"));
		onetimeAddress.setFgDelete(DbkrCommonVo.NO);

		return orderCreateService.saveOnetimeAddress(onetimeAddress);
	}

	@RequestMapping("/onetime_address/delete")
	@ResponseBody
	public Object deleteOnetimeAddress(@RequestParam("oaSeq") List<Integer> oaSeqs)
	{
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		OnetimeAddressDto onetimeAddress = new OnetimeAddressDto();
		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setRegUsername(getUsername());
		onetimeAddress.setFgDelete(DbkrCommonVo.YES);
		onetimeAddress.setOaSeqs(oaSeqs);

		return orderCreateService.deleteOnetimeAddress(onetimeAddress);
	}

	@RequestMapping("/onetime_address/postal_code")
	@ResponseBody
	public Object postalCode(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap rfcParamMap = new ErpDataMap(getErpUserInfo());
		rfcParamMap.put("country", onetimeAddress.getCountry());
		rfcParamMap.put("postalCode", onetimeAddress.getPostalCode());

		return orderCreateService.getPostalCode(rfcParamMap);
	}

	@RequestMapping("/onetime_address/tzone_list")
	@ResponseBody
	public Object tzone_list(@ModelAttribute OnetimeAddressDto onetimeAddress)
	{
		ErpDataMap rfcParamMap = new ErpDataMap();
		rfcParamMap.put("country", onetimeAddress.getCountry());
		rfcParamMap.put("transportZone", onetimeAddress.getTransportZone());

		return orderCreateService.getTransportZone(rfcParamMap);
	}

	@RequestMapping("/error_report")
	public String errorReport()
	{
		return DEFAULT_VIEW_URL;
	}
}
