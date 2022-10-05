package org.jwebppy.portal.iv.hq.parts.domestic.order.create.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.cart.dto.CartDto;
import org.jwebppy.portal.iv.hq.parts.cart.service.CartService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OrderCreateMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OrderHistoryHeaderObjectMapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper.OrderHistoryItemObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCreateService extends PartsDomesticGeneralService
{
	@Autowired
	private CartService cartService;

	@Autowired
	private OrderCreateMapper orderCreateMapper;

	@Autowired
	private OrderCreateGateService orderCreateGateService;

	public DataMap getHeaderInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USERINFO");

		rfcRequest.
			field().with(paramMap)
				.add("I_LANG", paramMap.getLangForSap())
				.addByKey(new Object[][] {
					{"LV_AUART", "orderType"},
					{"LV_KONDA", "priceGroup"},
					{"LV_VBTYP", "docType"}
				})
			.and()
			.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return new DataMap(simpleRfcTemplate.response(rfcRequest).getStructure("LS_EP002"));
	}

	@Cacheable(value = PortalCacheConfig.IVDO_ORDER_TYPE, key = "#paramMap", unless="#result == null")
	public DataList getOrderType(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_ORDERTYPE2");

		rfcRequest.
			field(new Object[][] {
				{"I_STATUS", "X"},
				{"I_VBTYP", CmStringUtils.defaultString(paramMap.get("pDocType"), CmStringUtils.defaultString(paramMap.get("docType"), "C"))},
				{"I_LANGU", paramMap.getLangForSap()}
			})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

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

		rfcRequest.
			field().with(paramMap)
				.add(new Object[][] {
					{"I_BGTYP", "P"},
					{"ILANGU", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()}
				});

		DataList dataList = simpleRfcTemplate.response(rfcRequest).getTable("ZSVV0004");

		DataList shiptoPartyList = new DataList();

		ListIterator it = dataList.listIterator();
		String customerNo = paramMap.getString("customerNo");
		String customerName = paramMap.getString("customerName");

		while (it.hasNext())
		{
			Map dataMap = (Map)it.next();
			boolean isOk = true;

			if (CmStringUtils.isNotEmpty(customerNo))
			{
				if (!CmStringUtils.containsIgnoreCaseAndEmpty((String)dataMap.get("KUNN2"), customerNo))
				{
					isOk = false;
				}
			}

			if (CmStringUtils.isNotEmpty(customerName))
			{
				if (!CmStringUtils.containsIgnoreCaseAndEmpty((String)dataMap.get("NAME1"), customerName))
				{
					isOk = false;
				}
			}

			if (isOk)
			{
				shiptoPartyList.add(dataMap);
			}
		}

		return shiptoPartyList;
	}

	public DataList getShippingInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SHIPPINGINFO");

		rfcRequest
			.field(new Object[][] {
					{"I_BGTYP", "P"},
					{"I_LANG", paramMap.getLangForSap()},
					{"I_KUNNR", paramMap.getString("customerNo")},
					{"I_USERID", paramMap.getUsername()}
				});

		return simpleRfcTemplate.response(rfcRequest).getTable("ZSST9100");
	}

	public Integer checkDuplicatedOrder(OrderDto order, Integer ohhSeq)
	{
		List<OrderHistoryHeaderDto> orderHistoryHeaderList = getOrderHistoryList(UserAuthenticationUtils.getUsername(), ohhSeq);

		if (CollectionUtils.isNotEmpty(orderHistoryHeaderList))
		{
			for (OrderHistoryHeaderDto orderHistoryHeader : orderHistoryHeaderList)
			{
				//주문 성공한 것만 가지고 중복 체크
				if (CmStringUtils.isEmpty(orderHistoryHeader.getSoNo()))
				{
					continue;
				}

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
			/* LT_ITEM */
			List<Map<String, Object>> items = new LinkedList<>();

			for (OrderItemDto orderItem : order.getOrderItems())
			{
				Map<String, Object> itemMap = new HashMap<>();
				itemMap.put("ITEM", orderItem.getLineNo());
				itemMap.put("MATERIAL", orderItem.getMaterialNo());
				itemMap.put("QTY", orderItem.getOrderQty().replaceAll(",", ""));
				itemMap.put("REQ_QTY", orderItem.getOrderQty().replaceAll(",", ""));
				itemMap.put("LOT_QTY", orderItem.getLotQty().replaceAll(",", ""));
				itemMap.put("UOM", orderItem.getUom());
				itemMap.put("AVAILABILITY", orderItem.getAvailability());
				itemMap.put("CCHECK", "1");

				items.add(itemMap);
			}

			RfcRequest rfcRequest = new RfcRequest();

			rfcRequest.
				field()
					.add(new Object[][] {
						{"LV_AUART", order.getOrderType()},
						{"LV_REMARK", order.getRemark()},
						{"I_USERID", order.getUsername()},
						{"I_LANGU", order.getLanguage()},
						{"I_BGTYP", "P"}
					})
				.and()
				.structure("LS_GENERAL")
					.add(new Object[][] {
						{"INCO1", order.getIncoterms1()},
						{"INCO2", order.getIncoterms2()},
						{"VSBED", order.getShippingCondition()},
						{"VDATU", CmDateFormatUtils.stripDateFormat(order.getRdd())},
						{"BSTNK", order.getPoNo()},
						{"COMPLETE_DELIVERY", order.getCompleDelivery()}
					})
				.and()
				.structure("LS_MAIN_HEAD")
					.add(new Object[][] {
						{"KUNNR", order.getSoldToNo()},
						{"KUNNR_NAME", order.getSoldToName()},
						{"KUNAG", order.getShipToNo()},
						{"KUNAG_NAME", order.getShipToName()},
						{"KONDA", order.getPriceGroup()},
						{"KVGR5", "20"}
					})
				.and()
				.table("LT_GERNERAL_ZTERM")
					.add("ZTERM", order.getPaymentTerms())
				.and()
				.table("LT_ITEM")
					.add(items);

			//Order History 저장
			Integer ohhSeq = saveOrderHistory(order);

			if ("C".equals(order.getDocType()))
			{
				//동일 주문 중복 생성 체크
				Integer duplOhhSeq = checkDuplicatedOrder(order, ohhSeq);

				if (ObjectUtils.isNotEmpty(duplOhhSeq))
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
				modifySuccessOrderHistoryHeader(ohhSeq, orderNo);

				if ("C".equals(order.getDocType()))
				{
					String refSystem = order.getRefSystem();

					if (CmStringUtils.equals(refSystem, "SB"))
					{
						try
						{
							//시스뱅크 연동 주문일 경우 상태 업데이트
							orderCreateGateService.doneSysbankOrder(order.getPoNo(), order.getRefSeq(), order.getSoldToNo());
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					else if (CmStringUtils.equals(refSystem, "cart"))
					{
						//장바구니에서 넘어온 주문은 주문 생성 완료 후 주문 완료된 자재를 삭제해 준다.
						CartDto cart = new CartDto();
						cart.setUSeq(UserAuthenticationUtils.getUSeq());
						cart.setOhhSeq(ohhSeq);

						List<String> materialNos = new ArrayList<>();

						for (OrderItemDto orderItem : order.getOrderItems())
						{
							materialNos.add(orderItem.getMaterialNo());
						}

						cart.setMaterialNos(materialNos);

						cartService.doneOrder(cart);
					}
				}
			}
			else
			{
				StringBuffer message = new StringBuffer();

				if (!"E".equals(rfcResponse.getString("E_MEG")))
				{
					DataList bapiret2 = rfcResponse.getTable("LT_BAPIRET2");

					if (CollectionUtils.isNotEmpty(bapiret2))
					{
						for (int i=0, size=bapiret2.size(); i<size; i++)
						{
							DataMap dataMap = new DataMap(bapiret2.get(i));

							if (dataMap.isEquals("ID", "V4"))
							{
								continue;
							}

							message.append(dataMap.getString("MESSAGE"));
						}

						rfcResponse.setObject("E_MEG", message.toString());
					}
				}

				//주문 실패 사유 저장
				modifyFailOrderHistoryHeader(ohhSeq, null, message.toString());
			}

			return rfcResponse;
		}

		return null;
	}

	public RfcResponse transferOrder(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_BAPI_SALESDOCUMENT_COPY");

		rfcRequest.
			field().with(paramMap)
				.addByKey(new Object[][] {
					{"DOCUMENTTYPE", "orderType"},
					{"SALESDOCUMENT", "orderNo"}
				});

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

	public OrderHistoryHeaderDto getOrderHistory(Integer ohhSeq)
	{
		return CmModelMapperUtils.mapToDto(OrderHistoryHeaderObjectMapper.INSTANCE, orderCreateMapper.findOrderHistoryHeader(ohhSeq));
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
}
