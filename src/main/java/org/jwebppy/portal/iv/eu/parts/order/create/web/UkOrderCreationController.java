package org.jwebppy.portal.iv.eu.parts.order.create.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.order.OrderGeneralController;
import org.jwebppy.portal.iv.eu.parts.order.OrderGeneralService;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOnetimeAddressDto;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOrderDto;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOrderItemDto;
import org.jwebppy.portal.iv.eu.parts.order.create.service.UkOrderCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/uk/scm/parts/order/create")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class UkOrderCreationController extends OrderGeneralController
{
	@Autowired
	private OrderGeneralService orderGeneralService;

	@Autowired
	private UkOrderCreationService orderCreationService;

	@RequestMapping("/order_form")
	public String orderForm(Model model, WebRequest webRequest)
	{
		model.addAttribute("docType", CmStringUtils.defaultIfEmpty(webRequest.getParameter("docType"), "C"));

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

		return orderCreationService.getHeaderInfo(rfcParamMap);
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
	public Object save(@ModelAttribute EuOrderDto order)
	{
		if (CmStringUtils.isNotEmpty(order.getMaterialNo()))
		{
			ErpDataMap userInfoMap = getErpUserInfo();

			order.setUsername(userInfoMap.getUsername());
			order.setLanguage(userInfoMap.getLang());
			order.setSalesOrg(userInfoMap.getSalesOrg());
			order.setDistChannel(userInfoMap.getDistChannel());
			order.setDivision(userInfoMap.getDivision());

			List<EuOrderItemDto> orderItems = new LinkedList<>();

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
					EuOrderItemDto orderItem = new EuOrderItemDto();

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

			RfcResponse rfcResponse = orderCreationService.save(order);

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

		return orderCreationService.getShipToParty(rfcParamMap);
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

		return orderCreationService.getShippingInfo(rfcParamMap);
	}

	@RequestMapping("/onetime_address_list")
	public Object onetimeAddressList(@RequestParam Map<String, Object> paramMap)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/onetime_address_list/data")
	@ResponseBody
	public Object onetimeAddressListData(@ModelAttribute EuOnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setRegUsername(getUsername());

		return orderCreationService.getOnetimeAddresses(onetimeAddress);
	}

	@RequestMapping("/onetime_address/duplication_check")
	@ResponseBody
	public Object checkOnetimeAddressDuplication(@ModelAttribute EuOnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setCustomerNo(CmStringUtils.stripStart(erpUserInfoMap.getCustomerNo(), "0"));
		onetimeAddress.setRegUsername(getUsername());

		List<EuOnetimeAddressDto> onetimeAddresses = orderCreationService.getOnetimeAddressesForDuplicationCheck(onetimeAddress);

		if (CollectionUtils.isNotEmpty(onetimeAddresses))
		{
			return onetimeAddresses.get(0);
		}

		return null;
	}

	@RequestMapping("/onetime_address/save")
	@ResponseBody
	public Object saveOnetimeAddress(@ModelAttribute EuOnetimeAddressDto onetimeAddress)
	{
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setCustomerNo(CmStringUtils.stripStart(erpUserInfoMap.getCustomerNo(), "0"));
		onetimeAddress.setFgDelete(EuCommonVo.NO);

		return orderCreationService.saveOnetimeAddress(onetimeAddress);
	}

	@RequestMapping("/onetime_address/delete")
	@ResponseBody
	public Object deleteOnetimeAddress(@RequestParam("oaSeq") List<Integer> oaSeqs)
	{
		ErpDataMap erpUserInfoMap = getErpUserInfo();

		EuOnetimeAddressDto onetimeAddress = new EuOnetimeAddressDto();
		onetimeAddress.setCorp(erpUserInfoMap.getCorpName());
		onetimeAddress.setRegUsername(getUsername());
		onetimeAddress.setFgDelete(EuCommonVo.YES);
		onetimeAddress.setOaSeqs(oaSeqs);

		return orderCreationService.deleteOnetimeAddress(onetimeAddress);
	}

	@RequestMapping("/onetime_address/postal_code")
	@ResponseBody
	public Object postalCode(@ModelAttribute EuOnetimeAddressDto onetimeAddress)
	{
		ErpDataMap rfcParamMap = new ErpDataMap(getErpUserInfo());
		rfcParamMap.put("country", onetimeAddress.getCountry());
		rfcParamMap.put("postalCode", onetimeAddress.getPostalCode());

		return orderCreationService.getPostalCode(rfcParamMap);
	}

	@RequestMapping("/onetime_address/tzone_list")
	@ResponseBody
	public Object tzone_list(@ModelAttribute EuOnetimeAddressDto onetimeAddress)
	{
		ErpDataMap rfcParamMap = new ErpDataMap();
		rfcParamMap.put("country", onetimeAddress.getCountry());
		rfcParamMap.put("transportZone", onetimeAddress.getTransportZone());

		return orderCreationService.getTransportZone(rfcParamMap);
	}

	@RequestMapping("/error_report")
	public String errorReport()
	{
		return DEFAULT_VIEW_URL;
	}
}
