package org.jwebppy.portal.iv.hq.parts.domestic.order.create.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.dao.support.IDataList;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.PricingResultDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.SimulationResultDto;
import org.springframework.stereotype.Service;

@Service
public class OrderSimulationService extends PartsDomesticGeneralService
{
	public RfcResponse getItemsFromGpes(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_BAPI_GET_SHOPPING_CART");

		rfcRequest.addField("I_USER_ID", paramMap.getUsername());
		rfcRequest.addField("I_DEALER_CODE", paramMap.getCustomerNo());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getLotQties(OrderDto order)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY");

		rfcRequest.
			field()
				.add(new Object[][] {
					{"I_LANGU", order.getLanguage()},
					{"I_BGTYP", "P"},
					{"I_USERID", order.getUsername()}
				});

		List<Map<String, Object>> items = new LinkedList<>();

		for (OrderItemDto orderItem : order.getOrderItems())
		{
			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("ITEM", orderItem.getLineNo());
			itemMap.put("MATERIAL", orderItem.getMaterialNo());
			itemMap.put("QTY", orderItem.getOrderQty().replaceAll(",", ""));

			items.add(itemMap);
		}

		rfcRequest.addTable("LT_ITEM", items);

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}

	public SimulationResultDto simulation(OrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			Map<String, Object> mainHead = new HashMap<>();
			mainHead.put("KUNNR", order.getSoldToNo());
			mainHead.put("KUNAG", CmStringUtils.defaultIfEmpty(order.getShipToNo(), order.getSoldToNo()));
			mainHead.put("KONDA", order.getPriceGroup());
			mainHead.put("KVGR5", "20");

			Map<String, Object> general = new HashMap<>();
			general.put("INCO1", order.getIncoterms1());
			general.put("INCO2", order.getIncoterms2());
			general.put("VSBED", order.getShippingCondition());

			Map<String, String> errorIgnore = new HashMap<>();
			errorIgnore.put("IGNORE", "X");
			errorIgnore.put("IGNORE_QTY", "X");
			errorIgnore.put("IGNORE_PRICE", "X");
			errorIgnore.put("IGNORE_DIVISION", "X");
			errorIgnore.put("IGNORE_PURCHASE", "X");

			Map<String, String> option = new HashMap<>();
			option.put("STOCK", "X");

			Map<String, Object> divMap = new HashMap<>();
			divMap.put("WERKS", "F160");

			List<Map<String, Object>> plants = new ArrayList<>();
			plants.add(divMap);

			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SIMUL_UPLOAD");

			rfcRequest
				.field()
					.add(new Object[][] {
						{"LV_AUART", order.getOrderType()},
						{"I_USERID", order.getUsername()},
						{"I_M", CmStringUtils.defaultString(order.getFgShowSubstitute(), "B")},//A:대체 미포함, B:대체 포함
						{"I_AVAIL", CmStringUtils.defaultString(order.getFgShowAvailability(), "X")},//X:가용성체크
						{"I_CREDIT", CmStringUtils.defaultString(order.getFgShowCredit(), "X")},//X:여신정보
						{"I_LANGU", order.getLanguage()},
						{"I_BGTYP", "P"}
					})
				.structure()
					.add(new Object[][] {
						{"LS_GENERAL", general},
						{"LS_MAIN_HEAD", mainHead},
						{"LS_ERROR_IGNORE", errorIgnore},
						{"LS_OPTION", option}
					})
				.table()
					.add("LT_STOCK", plants)
					.add("LT_ITEM", convertToLtItem(order.getOrderItems()));

			return makeSimulationResult(simpleRfcTemplate.response(rfcRequest));
		}

		return null;
	}

	public void setLotQty(OrderDto order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderItems()))
		{
			DataList lotQties = getLotQties(order);

			for (OrderItemDto orderItem : order.getOrderItems())
			{
				for (Object data : lotQties)
				{
					DataMap dataMap = new DataMap(data);

					if (dataMap.isEquals("MATERIAL", orderItem.getMaterialNo()))
					{

						orderItem.setLineNo("999990");
						orderItem.setLotQty(Integer.toString(dataMap.getInt("LOT_QTY")));
						break;
					}
				}
			}
		}
	}

	protected List<Map<String, Object>> convertToLtItem(List<OrderItemDto> orderItems)
	{
		List<Map<String, Object>> items = new LinkedList<>();

		for (OrderItemDto orderItem : orderItems)
		{
			if (CmStringUtils.isEmpty(orderItem.getMaterialNo()))
			{
				continue;
			}

			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("ITEM", orderItem.getLineNo());
			itemMap.put("MATERIAL", CmStringUtils.trimToEmpty(orderItem.getMaterialNo()).toUpperCase());
			itemMap.put("QTY", orderItem.getOrderQty());
			itemMap.put("REQ_QTY", orderItem.getOrderQty());

			items.add(itemMap);
		}

		return items;
	}

	protected SimulationResultDto makeSimulationResult(RfcResponse rfcResponse)
	{
		SimulationResultDto simulationResult = new SimulationResultDto();

		//Make header info
		makeHeaderInfo(rfcResponse, simulationResult);

		//Make pricing result
		List<PricingResultDto> pricingResults = simulationResult.getPricingResults();

		makePricingResult(rfcResponse.getTable("LT_PRICING_01"), pricingResults);
		makePricingResult(rfcResponse.getTable("LT_PRICING_02"), pricingResults);

		simulationResult.setPricingResults(pricingResults);

		//Make normal order items
		makeNormalOrderItems(rfcResponse, simulationResult);

		//Make error order items
		makeErrorOrderItems(rfcResponse, simulationResult);

		return simulationResult;
	}

	// Make error order items
	protected void makeErrorOrderItems(RfcResponse rfcResponse, SimulationResultDto simulationResult)
	{
		Map<String, IDataList> itemMap = new HashMap<>();
		itemMap.put("NON_EXIST", rfcResponse.getTable("LT_NON_EXIST"));
		//itemMap.put("WRONG_DIVISION", rfcResponse.getTable("LT_WRONG_DIVISION"));
		itemMap.put("NON_PURCHASE", rfcResponse.getTable("LT_NON_PURCHASE"));
		itemMap.put("NON_PRICE", rfcResponse.getTable("LT_NON_PRICE"));
		//itemMap.put("WRONG_ITEM", rfcResponse.getTable("LT_WRONG_ITEM"));//Sales Lot 오류
		itemMap.put("HAZARD_MATNR", rfcResponse.getTable("LT_HAZARD_MATNR"));

		Iterator<Entry<String, IDataList>> iterator = itemMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<String, IDataList> entry = iterator.next();

			IDataList items = entry.getValue();

			if (CollectionUtils.isNotEmpty(items))
			{
				List<OrderItemDto> errorOrderItems = simulationResult.getErrorOrderItems();

				for (int i=0, size=items.size(); i<size; i++)
				{
					DataMap dataMap = (DataMap)items.get(i);

					OrderItemDto orderItem = new OrderItemDto();
					orderItem.setMaterialNo(CmStringUtils.trimToEmpty(dataMap.getString("ERROR_MATNR")));
					orderItem.setSupplyMaterialNo(dataMap.getString("MATERIAL"));
					orderItem.setErrorType(entry.getKey());
					orderItem.setErrorCode(CmStringUtils.trimToEmpty(dataMap.getString("NOTCD")));
					orderItem.setErrorMessage(CmStringUtils.trimToEmpty(dataMap.getString("NOTCD_TEXT")));

					errorOrderItems.add(orderItem);
				}
			}
		}
	}

	protected void makeHeaderInfo(RfcResponse rfcResponse, SimulationResultDto simulationResult)
	{
		/* Make Header Info */
		DataMap mainHeadResult = rfcResponse.getStructure("LS_MAIN_HEAD_RESULT");

		FormatBuilder.with(mainHeadResult).decimalFormat(new String[] {"CREDIT", "NETWR"});
		FormatBuilder.with(mainHeadResult).weightFormat("TOTAL_WEIGHT");

		simulationResult.setTotalAmount(mainHeadResult.getString("NETWR"));
		simulationResult.setCredit(mainHeadResult.getString("CREDIT"));
		simulationResult.setCreditCurrency(mainHeadResult.getString("CREDIT_WAERK"));
		simulationResult.setTotalWeight(mainHeadResult.getString("TOTAL_WEIGHT"));
		simulationResult.setWeightUnit(mainHeadResult.getString("GEWEI"));
		simulationResult.setCurrency(mainHeadResult.getString("WAERK"));
		simulationResult.setFgChargeDocumentFree(mainHeadResult.getString("DOCUFEE"));

		DataMap generalResult = rfcResponse.getStructure("LS_GENERAL");
		simulationResult.setRdd(Formatter.getDefDateFormat(generalResult.getDate("VDATU")));
	}

	protected void makePricingResult(IDataList dataList, List<PricingResultDto> pricingResults)
	{
		for (Object obj : dataList)
		{
			DataMap dataMap = new DataMap(obj);

			PricingResultDto pricingResult = new PricingResultDto();
			pricingResult.setName(dataMap.getString("NAME"));
			pricingResult.setNetValue(Formatter.getDefDecimalFormat(dataMap.getString("NEWR")));
			pricingResult.setCurrency(dataMap.getString("WAERK"));

			pricingResults.add(pricingResult);
		}
	}

	// Make normal order items
	protected void makeNormalOrderItems(RfcResponse rfcResponse, SimulationResultDto simulationResult)
	{
		IDataList items = rfcResponse.getTable("LT_ITEM");

		if (CollectionUtils.isNotEmpty(items))
		{
			FormatBuilder.with(items)
				.qtyFormat(new String[] {"REQ_QTY", "LOT_QTY", "MIN_QTY", "ZBOQTY", "DIV"})
				.decimalFormat(new String[] {"NET_PRICE", "NET_VALUE", "NETPR"});

			List<OrderItemDto> normalOrderItems = new LinkedList<>();

			for (int i=0, size=items.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)items.get(i);

				int minQty = CmNumberUtils.toInt(dataMap.getString("MIN_QTY"), 1);

				if (minQty == 0)
				{
					minQty = 1;
				}

				int lotQty = CmNumberUtils.toInt(dataMap.getString("LOT_QTY"), 1);

				if (lotQty == 0)
				{
					lotQty = 1;
				}

				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(dataMap.getString("ITEM"));
				orderItem.setHigherLineNo(dataMap.getString("HILV"));
				orderItem.setMaterialNo(CmStringUtils.defaultIfEmpty(dataMap.getString("MATERIAL_ENT"), dataMap.getString("MATERIAL")));
				orderItem.setSupplyMaterialNo(dataMap.getString("MATERIAL"));
				orderItem.setDescription(dataMap.getString("MATERIAL_TEXT"));
				orderItem.setOrderQty(dataMap.getString("REQ_QTY"));
				orderItem.setSupplyQty(dataMap.getString("QTY"));
				orderItem.setAvailability(dataMap.getString("DIV"));
				orderItem.setMinOrderQty(Integer.toString(minQty));
				orderItem.setLotQty(Integer.toString(lotQty));
				orderItem.setUom(dataMap.getString("UOM"));
				orderItem.setNetPrice(dataMap.getString("NET_PRICE"));
				orderItem.setNetValue(dataMap.getString("NET_VALUE"));
				orderItem.setListPrice(dataMap.getString("NETPR"));
				orderItem.setMessage(dataMap.getString("MSG"));
				orderItem.setVendor(dataMap.getString("LIFNR"));
				orderItem.setPlant(dataMap.getString("PLANT"));
				orderItem.setStockDiv(dataMap.getString("DIV"));
				orderItem.setAmDevPart(dataMap.getString("AM_PART_NO"));
				orderItem.setBoQty(dataMap.getString("ZBOQTY"));

				normalOrderItems.add(orderItem);
			}

			simulationResult.setNormalOrderItems(simulationResult.makeCompactNormalOrderItems(normalOrderItems));
		}
	}
}
