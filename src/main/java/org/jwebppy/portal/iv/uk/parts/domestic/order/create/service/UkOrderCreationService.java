package org.jwebppy.portal.iv.uk.parts.domestic.order.create.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.uk.common.UkCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.common.service.UkPartsDomesticGeneralService;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOnetimeAddressDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderHistoryHeaderDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderHistoryItemDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderItemDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity.UkOnetimeAddressEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity.UkOrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity.UkOrderHistoryItemEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.mapper.UkOrderCreationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UkOrderCreationService extends UkPartsDomesticGeneralService
{
	@Autowired
	private UkOrderCreationMapper orderCreationMapper;

	public DataMap getHeaderInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USERINFO");

		rfcRequest.addField("I_LANG", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_SHIPTO", paramMap.getString("SHIPTO"));
		rfcRequest.addField("LV_AUART", paramMap.getString("AUART"));
		rfcRequest.addField("LV_KONDA", paramMap.getString("KONDA"));
		rfcRequest.addField("LV_VBTYP", paramMap.getString("VBTYP"));

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		return new DataMap(rfcResponse.getStructure("LS_EP002"));
	}

	public DataList getShipToParty(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SHIPTOPARTY");

		rfcRequest.addField("ILANGU", paramMap.getLang());
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

		rfcRequest.addField("I_LANG", paramMap.getLang());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_AUART", paramMap.getString("orderType"));
		rfcRequest.addField("I_KONDA", paramMap.getString("priceGroup"));
		rfcRequest.addField("I_KUNNR", paramMap.getCustomerNo());
		rfcRequest.addField("I_SHIPTO", paramMap.getString("shipToNo"));

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_0063");
	}

	public Integer checkDuplicatedOrder(UkOrderDto order, Integer ohhSeq)
	{
		List<UkOrderHistoryHeaderDto> orderHistoryHeaderList = getOrderHistoryList(getUsername(), ohhSeq);

		if (CollectionUtils.isNotEmpty(orderHistoryHeaderList))
		{
			for (UkOrderHistoryHeaderDto orderHistoryHeader : orderHistoryHeaderList)
			{
				if (orderHistoryHeader.isEquals(order))
				{
					return orderHistoryHeader.getOhhSeq();
				}
			}
		}

		return null;
	}

	@Caching(
			evict = {
					@CacheEvict (value = PortalCacheConfig.ORDER_DISPLAY, allEntries = true),
					@CacheEvict (value = PortalCacheConfig.BACKORDER, allEntries = true),
					@CacheEvict (value = PortalCacheConfig.ORDER_STATUS, allEntries = true)
			})
	public RfcResponse save(UkOrderDto order)
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
				UkOnetimeAddressEntity onetimeAddress = orderCreationMapper.findOnetimeAddressByOaSeq(order.getOaSeq());

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

			for (UkOrderItemDto orderItem : order.getOrderItems())
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
					ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();

					UkOnetimeAddressDto onetimeAddress = new UkOnetimeAddressDto();
					onetimeAddress.setCorp(erpUserContext.getCorpName());
					onetimeAddress.setOaSeq(order.getOaSeq());
					onetimeAddress.setFgUse(UkCommonVo.YES);
					onetimeAddress.setRegUsername(getUsername());

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

	public String checkValidPartsToOrder(UkOrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			/* LT_ITEM */
			List<Map<String, Object>> items = new LinkedList<>();

			for (UkOrderItemDto orderItem : order.getOrderItems())
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

	public List<UkOnetimeAddressDto> getOnetimeAddresses(UkOnetimeAddressDto onetimeAddress)
	{
		return CmModelMapperUtils.mapAll(orderCreationMapper.findAllOnetimeAddresses(onetimeAddress), UkOnetimeAddressDto.class);
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

	public boolean isDuplicatedOnetimeAddress(UkOnetimeAddressDto onetimeAddress)
	{
		if (CollectionUtils.isEmpty(orderCreationMapper.findAllOnetimeAddressDuplicationCheck(onetimeAddress)))
		{
			return false;
		}

		return true;
	}

	public List<UkOnetimeAddressDto> getOnetimeAddressesForDuplicationCheck(UkOnetimeAddressDto onetimeAddress)
	{
		return CmModelMapperUtils.mapAll(orderCreationMapper.findAllOnetimeAddressDuplicationCheck(onetimeAddress), UkOnetimeAddressDto.class);
	}

	public Integer saveOnetimeAddress(UkOnetimeAddressDto onetimeAddress)
	{
		UkOnetimeAddressEntity onetimeAddressEntity = CmModelMapperUtils.map(onetimeAddress, UkOnetimeAddressEntity.class);

		orderCreationMapper.insertOnetimeAddress(onetimeAddressEntity);

		return onetimeAddressEntity.getOaSeq();
	}

	public int modifyFgUseOfOnetimeAddress(UkOnetimeAddressDto onetimeAddress)
	{
		return orderCreationMapper.updateFgUseOfOnetimeAddress(CmModelMapperUtils.map(onetimeAddress, UkOnetimeAddressEntity.class));
	}

	public int deleteOnetimeAddress(UkOnetimeAddressDto onetimeAddress)
	{
		return orderCreationMapper.updateFgDeleteOfOnetimeAddress(CmModelMapperUtils.map(onetimeAddress, UkOnetimeAddressEntity.class));
	}

	public List<UkOrderHistoryHeaderDto> getOrderHistoryList(String regUsername, Integer ohhSeq)
	{
		UkOrderHistoryHeaderDto orderHistoryHeader = new UkOrderHistoryHeaderDto();
		orderHistoryHeader.setRegUsername(regUsername);
		orderHistoryHeader.setOhhSeq(ohhSeq);

		return CmModelMapperUtils.mapAll(orderCreationMapper.findAllOrderHistoryHeader(orderHistoryHeader), UkOrderHistoryHeaderDto.class);
	}

	@Transactional
	public Integer saveOrderHistory(UkOrderDto order)
	{
		UkOrderHistoryHeaderDto orderHistoryHeader = new UkOrderHistoryHeaderDto();
		orderHistoryHeader.setHeader(order);

		UkOrderHistoryHeaderEntity orderHistoryHeaderEntity = CmModelMapperUtils.map(orderHistoryHeader, UkOrderHistoryHeaderEntity.class);

		orderCreationMapper.insertOrderHistoryHeader(orderHistoryHeaderEntity);

		Integer ohhSeq = orderHistoryHeaderEntity.getOhhSeq();

		if (ohhSeq != null)
		{
			orderHistoryHeader.setItems(ohhSeq, order);

			List<UkOrderHistoryItemDto> orderHistoryItems = orderHistoryHeader.getOrderHistoryItems();

			if (CollectionUtils.isNotEmpty(orderHistoryItems))
			{
				for (UkOrderHistoryItemDto orderHistoryItem: orderHistoryItems)
				{
					orderCreationMapper.insertOrderHistoryItem(CmModelMapperUtils.map(orderHistoryItem, UkOrderHistoryItemEntity.class));
				}
			}
		}

		return ohhSeq;
	}

	public int modifySuccessOrderHistoryHeader(Integer ohhSeq, String soNo)
	{
		UkOrderHistoryHeaderEntity orderHistoryHeader = new UkOrderHistoryHeaderEntity();
		orderHistoryHeader.setOhhSeq(ohhSeq);
		orderHistoryHeader.setSoNo(soNo);

		return orderCreationMapper.updateSuccessOrderHistoryHeader(orderHistoryHeader);
	}

	public int modifyFailOrderHistoryHeader(Integer ohhSeq, Integer duplOhhSeq, String errorMsg)
	{
		UkOrderHistoryHeaderEntity orderHistoryHeader = new UkOrderHistoryHeaderEntity();
		orderHistoryHeader.setOhhSeq(ohhSeq);
		orderHistoryHeader.setDuplOhhSeq(duplOhhSeq);
		orderHistoryHeader.setErrorMsg(errorMsg);

		return orderCreationMapper.updateFailOrderHistoryHeader(orderHistoryHeader);
	}

	public static void main(String[] args)
	{
		long startTime = 1597054475358L;
		LocalDateTime localDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

		System.out.println(CmDateFormatUtils.format(localDateTime));
	}
}
