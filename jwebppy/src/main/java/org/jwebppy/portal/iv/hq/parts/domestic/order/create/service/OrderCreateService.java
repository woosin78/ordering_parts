package org.jwebppy.portal.iv.hq.parts.domestic.order.create.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpUserContext;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OnetimeAddressDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OnetimeAddressEntity;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OnetimeAddressObjectMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OrderCreateMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OrderHistoryHeaderObjectMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OrderHistoryItemObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCreateService
{
	@Autowired
	private OrderCreateMapper orderCreateMapper;

	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public DataMap getHeaderInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USERINFO");

		rfcRequest.addField("I_LANG", paramMap.getLangForSap());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("LV_AUART", paramMap.getString("AUART"));
		rfcRequest.addField("LV_KONDA", paramMap.getString("KONDA"));
		rfcRequest.addField("LV_VBTYP", paramMap.getString("VBTYP"));

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		return new DataMap(rfcResponse.getStructure("LS_EP002"));
	}

	public DataList getOrderType(ErpDataMap paramMap)
	{
		String docType = CmStringUtils.defaultString(paramMap.get("pDocType"), CmStringUtils.defaultString(paramMap.get("docType"), "C"));

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_ORDERTYPE2");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_LANGU", paramMap.getLangForSap());
		rfcRequest.addField("I_STATUS", "X");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_VBTYP", docType);

		DataList dataList = simpleRfcTemplate.response(rfcRequest).getTable("T_ZSSV0002");

		DataList tmpOrderTypes = new DataList();

		for (Object orderType : dataList)
		{
			DataMap orderTypeMap = new DataMap(orderType);

			if (orderTypeMap.isEquals("AUART", "YDIR") || orderTypeMap.isEquals("AUART", "YEIR"))
			{
				continue;
			}

			tmpOrderTypes.add(orderTypeMap);
		}

		return tmpOrderTypes;
	}

	public DataList getShipToParty(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SHIPTOPARTY");

		rfcRequest.addField("ILANGU", paramMap.getLangForSap());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		DataList dataList = simpleRfcTemplate.response(rfcRequest).getTable("ZSVV0004");

		if (paramMap.isEmptyValue("customerNo") && paramMap.isEmptyValue("customerName"))
		{
			return dataList;
		}

		String customerNo = paramMap.getString("customerNo");
		String customerName = paramMap.getString("customerName");

		DataList tmpDataList = new DataList();

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			if (CmStringUtils.containsIgnoreCaseAndEmpty(dataMap.getString("KUNN2"), customerNo) || CmStringUtils.containsIgnoreCaseAndEmpty(dataMap.getString("NAME1"), customerName))
			{
				tmpDataList.add(dataMap);
			}
		}

		return tmpDataList;
	}

	public DataList getShippingInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SHIPPINGINFO2");

		rfcRequest.addField("I_LANG", paramMap.getLangForSap());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_AUART", paramMap.getString("orderType"));
		rfcRequest.addField("I_KONDA", paramMap.getString("priceGroup"));
		rfcRequest.addField("I_KUNNR", paramMap.getCustomerNo());

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_0063");
	}

	public Integer checkDuplicatedOrder(OrderDto order, Integer ohhSeq)
	{
		List<OrderHistoryHeaderDto> orderHistoryHeaderList = getOrderHistoryList(UserAuthenticationUtils.getUsername(), ohhSeq);

		if (CollectionUtils.isNotEmpty(orderHistoryHeaderList))
		{
			for (OrderHistoryHeaderDto orderHistoryHeader : orderHistoryHeaderList)
			{
				if (orderHistoryHeader.isEquals(order))
				{
					return orderHistoryHeader.getOhhSeq();
				}
			}
		}

		return null;
	}

	public RfcResponse save(OrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			RfcRequest rfcRequest = new RfcRequest();

			rfcRequest.addField("LV_AUART", order.getOrderType());
			rfcRequest.addField("LV_REMARK", order.getRemark());
			rfcRequest.addField("I_USERID", order.getUsername());
			rfcRequest.addField("I_LANGU", order.getLanguage());
			rfcRequest.addField("I_BGTYP", "P");

			/* LS_GENERAL */
			Map<String, Object> generalMap = new HashMap<>();
			generalMap.put("INCO1", order.getIncoterms1());
			generalMap.put("INCO2", order.getIncoterms2());
			generalMap.put("VSBED", order.getShippingCondition());
			generalMap.put("VDATU", CmStringUtils.trimToEmpty(order.getRdd()).replaceAll("\\.", ""));
			generalMap.put("BSTNK", order.getPoNo());
			generalMap.put("COMPLETE_DELIVERY", order.getCompleDelivery());

			rfcRequest.addStructure("LS_GENERAL", generalMap);

			/* LS_MAIN_HEAD*/
			Map<String, Object> mainHeadMap = new HashMap<>();
			mainHeadMap.put("KUNNR", order.getSoldToNo());
			mainHeadMap.put("KUNNR_NAME", order.getSoldToName());
			mainHeadMap.put("KUNAG", order.getShipToNo());
			mainHeadMap.put("KUNAG_NAME", order.getShipToName());
			mainHeadMap.put("KONDA", order.getPriceGroup());
			mainHeadMap.put("KVGR5", "20");

			rfcRequest.addStructure("LS_MAIN_HEAD", mainHeadMap);

			/* LT_GERNERAL_ZTERM */
			Map<String, Object> termMap = new HashMap<>();
			termMap.put("ZTERM", order.getPaymentTerms());

			List<Map<String, Object>> generalTerms = new ArrayList<>();
			generalTerms.add(termMap);

			rfcRequest.addTable("LT_GERNERAL_ZTERM", generalTerms);

			/* LS_EP_ONETIME */
			if (order.getOaSeq() != null)
			{
				OnetimeAddressEntity onetimeAddress = orderCreateMapper.findOnetimeAddressByOaSeq(order.getOaSeq());

				if (onetimeAddress == null)
				{
					return null;
				}
				else
				{
					Map<String, Object> onetimeAddressMap = new HashMap<>();
					onetimeAddressMap.put("NAME", onetimeAddress.getName());
					onetimeAddressMap.put("CITY", onetimeAddress.getCity());
					onetimeAddressMap.put("STREET", onetimeAddress.getStreet());
					onetimeAddressMap.put("COUNTRY", onetimeAddress.getCountry());
					onetimeAddressMap.put("POSTL_CODE", onetimeAddress.getPostalCode());
					onetimeAddressMap.put("TRANSPZONE", onetimeAddress.getTransportZone());

					rfcRequest.addStructure("LS_EP_ONETIME", onetimeAddressMap);
				}
			}

			/* LT_ITEM */
			List<Map<String, Object>> items = new LinkedList<>();

			for (OrderItemDto orderItem : order.getOrderItems())
			{
				Map<String, Object> itemMap = new HashMap<>();
				itemMap.put("ITEM", orderItem.getLineNo());
				itemMap.put("MATERIAL", orderItem.getMaterialNo());
				itemMap.put("QTY", orderItem.getOrderQty());
				itemMap.put("REQ_QTY", orderItem.getOrderQty());
				itemMap.put("LOT_QTY", orderItem.getLotQty());
				itemMap.put("UOM", orderItem.getUom());
				itemMap.put("AVAILABILITY", orderItem.getAvailability());
				itemMap.put("CCHECK", "1");

				items.add(itemMap);
			}

			rfcRequest.addTable("LT_ITEM", items);

			//Order History 저장
			Integer ohhSeq = saveOrderHistory(order);

			if ("C".equals(order.getDocType()))
			{
				//동일 주문 중복 생성 체크
				Integer duplOhhSeq = checkDuplicatedOrder(order, ohhSeq);

				if (duplOhhSeq != null)
				{
					//주문 실패 사유 저장
					modifyFailOrderHistoryHeader(ohhSeq, duplOhhSeq, null);

					RfcResponse rfcResponse = new RfcResponse();
					rfcResponse.setObject("E_MEG", "DUPLICATED_ORDER");

					return rfcResponse;
				}

				rfcRequest.setFunctionName("ZSS_PARA_DIV_EP_ORDER_SAVE");
			}
			else
			{
				rfcRequest.setFunctionName("ZSS_PARA_DIV_EP_INQURY_CRE");
			}

			RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

			String orderNo = CmStringUtils.trimToEmpty(rfcResponse.getString("LV_VENLR"));

			if (orderNo.length() > 5)
			{
				//Onetime Address 로 주문 했을 경우
				if (order.getOaSeq() != null)
				{
					PartsErpUserContext erpUserContext = (PartsErpUserContext)UserAuthenticationUtils.getUserDetails().getErpUserContext();

					OnetimeAddressDto onetimeAddress = new OnetimeAddressDto();
					onetimeAddress.setCorp(erpUserContext.getCorp());
					onetimeAddress.setOaSeq(order.getOaSeq());
					onetimeAddress.setFgUse(PartsCommonVo.YES);
					onetimeAddress.setRegUsername(UserAuthenticationUtils.getUsername());

					modifyFgUseOfOnetimeAddress(onetimeAddress);
				}

				modifySuccessOrderHistoryHeader(ohhSeq, orderNo);
			}
			else
			{
				if (!"E".equals(rfcResponse.getString("E_MEG")))
				{
					DataList bapiret2 = rfcResponse.getTable("LT_BAPIRET2");

					if (CollectionUtils.isNotEmpty(bapiret2))
					{
						DataMap dataMap = new DataMap(bapiret2.get(0));
						rfcResponse.setObject("E_MEG", dataMap.getString("MESSAGE"));
					}
				}

				//주문 실패 사유 저장
				modifyFailOrderHistoryHeader(ohhSeq, null, rfcResponse.getString("E_MEG"));
			}

			return rfcResponse;
		}

		return null;
	}

	public RfcResponse transferOrder(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_BAPI_SALESDOCUMENT_COPY");

		rfcRequest.addField("DOCUMENTTYPE", paramMap.getString("orderType"));
		rfcRequest.addField("SALESDOCUMENT", paramMap.getString("orderNo"));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public String checkValidPartsToOrder(OrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			/* LT_ITEM */
			List<Map<String, Object>> items = new LinkedList<>();

			for (OrderItemDto orderItem : order.getOrderItems())
			{
				Map<String, Object> itemMap = new HashMap<>();
				itemMap.put("MATNR", orderItem.getMaterialNo());

				items.add(itemMap);
			}

			RfcRequest rfcRequest = new RfcRequest("ZSS_RECV_CHECK_PARTSLIST");

			rfcRequest.addTable("T_LIST", items);
			rfcRequest.addOutputParameter("O_MESG");

			RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

			return rfcResponse.getString("O_MESG");
		}

		return null;
	}

	public List<OnetimeAddressDto> getOnetimeAddresses(OnetimeAddressDto onetimeAddress)
	{
		return CmModelMapperUtils.mapToDto(OnetimeAddressObjectMapper.INSTANCE, orderCreateMapper.findAllOnetimeAddresses(onetimeAddress));
	}

	public String getPostalCode(ErpDataMap erpParamMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_SEND_DIV_TZONE_TO_PORTAL");

		rfcRequest.addField("LAND1", erpParamMap.get("country"));
		rfcRequest.addField("POST_CODE1", erpParamMap.getString("postalCode").toUpperCase());

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		return rfcResponse.getString("ZONE1");
	}

	public String getTransportZone(ErpDataMap erpParamMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_SEND_DIV_LAND_TO_PORTAL");

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		String country = erpParamMap.getString("country");
		String transportZone = erpParamMap.getString("transportZone");

		for (Object data : rfcResponse.getTable("GT_TZONE"))
		{
			DataMap dataMap = new DataMap(data);

			if (dataMap.isEquals("LAND1", country) && dataMap.isEquals("ZONE1", transportZone))
			{
				return dataMap.getString("VTEXT");
			}
		}

		return null;
	}

	public boolean isDuplicatedOnetimeAddress(OnetimeAddressDto onetimeAddress)
	{
		if (CollectionUtils.isEmpty(orderCreateMapper.findAllSameOnetimeAddresses(onetimeAddress)))
		{
			return false;
		}

		return true;
	}

	public List<OnetimeAddressDto> getOnetimeAddressesForDuplicationCheck(OnetimeAddressDto onetimeAddress)
	{
		return CmModelMapperUtils.mapToDto(OnetimeAddressObjectMapper.INSTANCE, orderCreateMapper.findAllSameOnetimeAddresses(onetimeAddress));
	}

	public Integer saveOnetimeAddress(OnetimeAddressDto onetimeAddress)
	{
		OnetimeAddressEntity onetimeAddressEntity = CmModelMapperUtils.mapToEntity(OnetimeAddressObjectMapper.INSTANCE, onetimeAddress);

		orderCreateMapper.insertOnetimeAddress(onetimeAddressEntity);

		return onetimeAddressEntity.getOaSeq();
	}

	public int modifyFgUseOfOnetimeAddress(OnetimeAddressDto onetimeAddress)
	{
		return orderCreateMapper.updateFgUseOfOnetimeAddress(CmModelMapperUtils.mapToEntity(OnetimeAddressObjectMapper.INSTANCE, onetimeAddress));
	}

	public int deleteOnetimeAddress(OnetimeAddressDto onetimeAddress)
	{
		return orderCreateMapper.updateFgDeleteOfOnetimeAddress(CmModelMapperUtils.mapToEntity(OnetimeAddressObjectMapper.INSTANCE, onetimeAddress));
	}

	public List<OrderHistoryHeaderDto> getOrderHistoryList(String regUsername, Integer ohhSeq)
	{
		OrderHistoryHeaderDto orderHistoryHeader = new OrderHistoryHeaderDto();
		orderHistoryHeader.setRegUsername(regUsername);
		orderHistoryHeader.setOhhSeq(ohhSeq);

		return CmModelMapperUtils.mapToDto(OrderHistoryHeaderObjectMapper.INSTANCE, orderCreateMapper.findAllOrderHistoryHeader(orderHistoryHeader));
	}

	@Transactional
	public Integer saveOrderHistory(OrderDto order)
	{
		OrderHistoryHeaderDto orderHistoryHeader = new OrderHistoryHeaderDto();
		orderHistoryHeader.setHeader(order);

		OrderHistoryHeaderEntity orderHistoryHeaderEntity = CmModelMapperUtils.mapToEntity(OrderHistoryHeaderObjectMapper.INSTANCE, orderHistoryHeader);

		orderCreateMapper.insertOrderHistoryHeader(orderHistoryHeaderEntity);

		Integer ohhSeq = orderHistoryHeaderEntity.getOhhSeq();

		if (ohhSeq != null)
		{
			orderHistoryHeader.setItems(ohhSeq, order);

			List<OrderHistoryItemDto> orderHistoryItems = orderHistoryHeader.getOrderHistoryItems();

			if (CollectionUtils.isNotEmpty(orderHistoryItems))
			{
				for (OrderHistoryItemDto orderHistoryItem: orderHistoryItems)
				{
					orderCreateMapper.insertOrderHistoryItem(CmModelMapperUtils.mapToEntity(OrderHistoryItemObjectMapper.INSTANCE, orderHistoryItem));
				}
			}
		}

		return ohhSeq;
	}

	public int modifySuccessOrderHistoryHeader(Integer ohhSeq, String soNo)
	{
		OrderHistoryHeaderEntity orderHistoryHeader = new OrderHistoryHeaderEntity();
		orderHistoryHeader.setOhhSeq(ohhSeq);
		orderHistoryHeader.setSoNo(soNo);

		return orderCreateMapper.updateSuccessOrderHistoryHeader(orderHistoryHeader);
	}

	public int modifyFailOrderHistoryHeader(Integer ohhSeq, Integer duplOhhSeq, String errorMsg)
	{
		OrderHistoryHeaderEntity orderHistoryHeader = new OrderHistoryHeaderEntity();
		orderHistoryHeader.setOhhSeq(ohhSeq);
		orderHistoryHeader.setDuplOhhSeq(duplOhhSeq);
		orderHistoryHeader.setErrorMsg(errorMsg);

		return orderCreateMapper.updateFailOrderHistoryHeader(orderHistoryHeader);
	}

	public static void main(String[] args)
	{
		long startTime = 1597054475358L;
		LocalDateTime localDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

		System.out.println(CmDateFormatUtils.format(localDateTime));
	}
}
