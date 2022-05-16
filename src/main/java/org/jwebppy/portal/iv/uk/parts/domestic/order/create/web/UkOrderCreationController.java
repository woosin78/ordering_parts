package org.jwebppy.portal.iv.uk.parts.domestic.order.create.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.uk.parts.domestic.order.UkOrderCommonService;
import org.jwebppy.portal.iv.uk.parts.domestic.order.UkOrderGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderItemDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.service.UkOrderCreationService;
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
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/order/create")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class UkOrderCreationController extends UkOrderGeneralController
{
	@Autowired
	private UkOrderCommonService orderCommonService;

	@Autowired
	private UkOrderCreationService ukOrderCreationService;

	@RequestMapping("/order_form")
	public String orderForm(Model model, WebRequest webRequest)
	{
		model.addAttribute("docType", CmStringUtils.defaultIfEmpty(webRequest.getParameter("docType"), "C"));
		model.addAttribute("from", webRequest.getParameter("from"));
		model.addAttribute("materialNosFrom", webRequest.getParameter("materialNosFrom"));
		model.addAttribute("orderQtiesFrom", webRequest.getParameter("orderQtiesFrom"));
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
		rfcParamMap.put("SHIPTO", paramMap.get("shipToNo"));

		return ukOrderCreationService.getHeaderInfo(rfcParamMap);
	}

	@RequestMapping("/order_type/data")
	@ResponseBody
	public Object orderTypeData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		return orderCommonService.getOrderType(rfcParamMap);
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute UkOrderDto order) throws Exception
	{
//		if ("CF".equals(order.getShippingCondition()) && !("YUSO".equals(order.getOrderType()) || "YUEO".equals(order.getOrderType())))
//		{
//			Map<String, Object> resultMap = new HashMap<>();
//			resultMap.put("errorMsg", "");
//
//			return resultMap;
//		}

		if (CmStringUtils.isNotEmpty(order.getMaterialNo()))
		{
			ErpDataMap userInfoMap = getErpUserInfo();

			order.setUsername(userInfoMap.getUsername());
			order.setLanguage(userInfoMap.getLang());
			order.setSalesOrg(userInfoMap.getSalesOrg());
			order.setDistChannel(userInfoMap.getDistChannel());
			order.setDivision(userInfoMap.getDivision());

			List<UkOrderItemDto> orderItems = new LinkedList<>();

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
					UkOrderItemDto orderItem = new UkOrderItemDto();

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

			RfcResponse rfcResponse = ukOrderCreationService.save(order);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("orderNo", rfcResponse.getString("LV_VENLR"));
			resultMap.put("errorMsg", rfcResponse.getString("E_MEG"));

			return resultMap;
		}

		return null;
	}

	@RequestMapping("/ship_to_party_list")
	public Object shipToPartyList(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/ship_to_party_list/data")
	@ResponseBody
	public Object shipToPartyListData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("customerNo", paramMap.get("pCustomerNo"));
		rfcParamMap.put("customerName", paramMap.get("pCustomerName"));

		return ukOrderCreationService.getShipToParty(rfcParamMap);
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
		rfcParamMap.put("shipToNo", paramMap.get("shipToNo"));

		return ukOrderCreationService.getShippingInfo(rfcParamMap);
	}

	@RequestMapping("/transfer_order")
	public Object transferOrder(@RequestParam Map<String, Object> paramMap)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/transfer_order/save")
	@ResponseBody
	public Object saveTransferOrder(@RequestParam Map<String, Object> paramMap)
	{
		DataMap rfcParamMap = new DataMap(paramMap);

		RfcResponse rfcResponse = ukOrderCreationService.transferOrder(rfcParamMap);
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

	@RequestMapping("/error_report")
	public String errorReport()
	{
		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/valid_check")
	@ResponseBody
	public Object validCheck(@ModelAttribute UkOrderDto order)
	{
		//현지 테스트 중. 개발 환경에서만 동작하도록 조치
		if (isProduction() || CmStringUtils.equalsIgnoreCase(getUsername(), "P_EUFL_HANA") || CmStringUtils.equalsIgnoreCase(getUsername(), "P_UKFL_HANA"))
		{
			return EMPTY_RETURN_VALUE;
		}

		String[] materialNos = CmStringUtils.split(order.getMaterialNo(), "\\^");

		List<UkOrderItemDto> orderItems = new LinkedList<>();

		for (int i=0, length=materialNos.length; i<length; i++)
		{
			if (CmStringUtils.isNotEmpty(materialNos[i]))
			{
				UkOrderItemDto orderItem = new UkOrderItemDto();
				orderItem.setLineNo(CmStringUtils.leftPad(i+1, 6, "0"));
				orderItem.setMaterialNo(materialNos[i]);

				orderItems.add(orderItem);
			}
		}

		order.setOrderItems(orderItems);

		return ukOrderCreationService.checkValidPartsToOrder(order);
	}
}
