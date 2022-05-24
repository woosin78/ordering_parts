package org.jwebppy.portal.iv.hq.parts.export.order.create.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderHistoryItemDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderItemDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.entity.ExOrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.hq.parts.export.order.create.mapper.ExOrderCreateMapper;
import org.jwebppy.portal.iv.hq.parts.export.order.create.mapper.ExOrderHistoryHeaderObjectMapper;
import org.jwebppy.portal.iv.hq.parts.export.order.create.mapper.ExOrderHistoryItemObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExOrderCreateService extends PartsExportGeneralService
{
	//@Autowired
	//private CacheHelper cacheHelper;

	//@Autowired
	//private PortalCacheConfig portalCacheConfig;

	@Autowired
	private ExOrderCreateMapper orderCreateMapper;

	public DataMap getHeaderInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USERINFO");

		rfcRequest.
			field().with(paramMap)
				.add(new Object[][] {
					{"I_BGTYP", "P"},
					{"I_LANG", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()},
				})
				.addByKey(new Object[][] {
					{"LV_AUART", "orderType"},
					{"LV_KONDA", "priceGroup"},
					{"LV_VBTYP", "docType"}
				});

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		return new DataMap(rfcResponse.getStructure("LS_EP002"));
	}

	public DataList getOrderType(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_ORDERTYPE2");

		rfcRequest.
			field(new Object[][] {
				{"I_BGTYP", "P"},
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
					{"I_LANG", paramMap.getLangForSap()}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest).getTable("ZSST9100");
	}

	public Integer checkDuplicatedOrder(ExOrderDto order, Integer ohhSeq)
	{
		List<ExOrderHistoryHeaderDto> orderHistoryHeaderList = getOrderHistoryList(UserAuthenticationUtils.getUsername(), ohhSeq);

		if (CollectionUtils.isNotEmpty(orderHistoryHeaderList))
		{
			for (ExOrderHistoryHeaderDto orderHistoryHeader : orderHistoryHeaderList)
			{
				if (orderHistoryHeader.isEquals(order))
				{
					return orderHistoryHeader.getOhhSeq();
				}
			}
		}

		return null;
	}

	public RfcResponse save(ExOrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			/* LT_ITEM */
			List<Map<String, Object>> items = new LinkedList<>();

			for (ExOrderItemDto orderItem : order.getOrderItems())
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
				modifySuccessOrderHistoryHeader(ohhSeq, orderNo);

				//cacheHelper.evict(portalCacheConfig.getCacheNames());
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

	public List<ExOrderHistoryHeaderDto> getOrderHistoryList(String regUsername, Integer ohhSeq)
	{
		ExOrderHistoryHeaderDto orderHistoryHeader = new ExOrderHistoryHeaderDto();
		orderHistoryHeader.setRegUsername(regUsername);
		orderHistoryHeader.setOhhSeq(ohhSeq);

		return CmModelMapperUtils.mapToDto(ExOrderHistoryHeaderObjectMapper.INSTANCE, orderCreateMapper.findAllOrderHistoryHeader(orderHistoryHeader));
	}

	@Transactional
	public Integer saveOrderHistory(ExOrderDto order)
	{
		ExOrderHistoryHeaderDto orderHistoryHeader = new ExOrderHistoryHeaderDto();
		orderHistoryHeader.setHeader(order);

		ExOrderHistoryHeaderEntity orderHistoryHeaderEntity = CmModelMapperUtils.mapToEntity(ExOrderHistoryHeaderObjectMapper.INSTANCE, orderHistoryHeader);

		orderCreateMapper.insertOrderHistoryHeader(orderHistoryHeaderEntity);

		Integer ohhSeq = orderHistoryHeaderEntity.getOhhSeq();

		if (ohhSeq != null)
		{
			orderHistoryHeader.setItems(ohhSeq, order);

			List<ExOrderHistoryItemDto> orderHistoryItems = orderHistoryHeader.getOrderHistoryItems();

			if (CollectionUtils.isNotEmpty(orderHistoryItems))
			{
				for (ExOrderHistoryItemDto orderHistoryItem: orderHistoryItems)
				{
					orderCreateMapper.insertOrderHistoryItem(CmModelMapperUtils.mapToEntity(ExOrderHistoryItemObjectMapper.INSTANCE, orderHistoryItem));
				}
			}
		}

		return ohhSeq;
	}

	public int modifySuccessOrderHistoryHeader(Integer ohhSeq, String soNo)
	{
		ExOrderHistoryHeaderEntity orderHistoryHeader = new ExOrderHistoryHeaderEntity();
		orderHistoryHeader.setOhhSeq(ohhSeq);
		orderHistoryHeader.setSoNo(soNo);

		return orderCreateMapper.updateSuccessOrderHistoryHeader(orderHistoryHeader);
	}

	public int modifyFailOrderHistoryHeader(Integer ohhSeq, Integer duplOhhSeq, String errorMsg)
	{
		ExOrderHistoryHeaderEntity orderHistoryHeader = new ExOrderHistoryHeaderEntity();
		orderHistoryHeader.setOhhSeq(ohhSeq);
		orderHistoryHeader.setDuplOhhSeq(duplOhhSeq);
		orderHistoryHeader.setErrorMsg(errorMsg);

		return orderCreateMapper.updateFailOrderHistoryHeader(orderHistoryHeader);
	}
}
