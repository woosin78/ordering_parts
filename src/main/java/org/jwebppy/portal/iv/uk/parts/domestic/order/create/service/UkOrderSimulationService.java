package org.jwebppy.portal.iv.uk.parts.domestic.order.create.service;

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
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.common.service.UkPartsDomesticGeneralService;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderItemDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkPricingResultDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkSimulationResultDto;
import org.springframework.stereotype.Service;

@Service
public class UkOrderSimulationService extends UkPartsDomesticGeneralService
{
	public RfcResponse getItemsFromGpes(ErpDataMap paramMap)
	{
		//RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_GET_SHOPPING_CART");
		RfcRequest rfcRequest = new RfcRequest("Z_SS_BAPI_GET_SHOPPING_CART");

		rfcRequest.addField("I_USER_ID", paramMap.getUsername());
		rfcRequest.addField("I_DEALER_CODE", paramMap.getCustomerNo());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getLotQties(UkOrderDto order)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY");

		rfcRequest.addField("I_LANGU", order.getLanguage());
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_USERID", order.getUsername());

		List<Map<String, Object>> items = new LinkedList<>();

		for (UkOrderItemDto orderItem : order.getOrderItems())
		{
			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("ITEM", orderItem.getLineNo());
			itemMap.put("MATERIAL", orderItem.getMaterialNo());
			itemMap.put("QTY", orderItem.getOrderQty());

			items.add(itemMap);
		}

		rfcRequest.addTable("LT_ITEM", items);

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}

	public UkSimulationResultDto simulation(UkOrderDto order)
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

			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SIMUL_UPLOAD");

			rfcRequest.addField("LV_AUART", order.getOrderType());
			rfcRequest.addField("I_USERID", order.getUsername());
			rfcRequest.addField("I_M", CmStringUtils.defaultString(order.getFgShowSubstitute(), "B")); //A:대체 미포함, B:대체 포함
			rfcRequest.addField("I_AVAIL", CmStringUtils.defaultString(order.getFgShowAvailability(), "X")); //X:가용성체크
			rfcRequest.addField("I_CREDIT", CmStringUtils.defaultString(order.getFgShowCredit(), "X")); //X:여신정보

			rfcRequest.addField("I_LANGU", order.getLanguage());
			rfcRequest.addField("I_BGTYP", "P");
			rfcRequest.addStructure("LS_GENERAL", general);
			rfcRequest.addStructure("LS_MAIN_HEAD", mainHead);
			rfcRequest.addStructure("LS_ERROR_IGNORE", errorIgnore);
			rfcRequest.addStructure("LS_OPTION", "STOCK", "X");//각 PDC 별 재고 정보 가져오기

			Map<String, String> divMap = new HashMap<>();
			divMap.put("WERKS", "7260");

			List<Map<String, String>> plants = new ArrayList<>();
			plants.add(divMap);

			rfcRequest.addTable("LT_STOCK", plants);

			if (CmStringUtils.equals(EuCommonVo.YES, order.getFgShowListPrice()))
			{
				rfcRequest.addStructure("LS_OPTION", "PRICE", "X");//X:List Price 정보 가져오기
			}

			List<Map<String, Object>> items = new LinkedList<>();

			for (UkOrderItemDto orderItem : order.getOrderItems())
			{
				Map<String, Object> itemMap = new HashMap<>();
				itemMap.put("ITEM", orderItem.getLineNo());
				itemMap.put("MATERIAL", orderItem.getMaterialNo());
				itemMap.put("QTY", orderItem.getOrderQty());

				items.add(itemMap);
			}

			adjustOrderQty(order);

			rfcRequest.addTable("LT_ITEM", convertToLtItem(order.getOrderItems()));

			return makeSimulationResult(simpleRfcTemplate.response(rfcRequest));
		}

		return new UkSimulationResultDto();
	}

	protected void adjustOrderQty(UkOrderDto order)
	{
		DataList lotQties = getLotQties(order);

		for (UkOrderItemDto orderItem : order.getOrderItems())
		{
			for (Object data : lotQties)
			{
				DataMap dataMap = new DataMap(data);

				if (dataMap.isEquals("MATERIAL", orderItem.getMaterialNo()))
				{
					orderItem.setLotQty(dataMap.getString("LOT_QTY"));
					break;
				}
			}
		}
	}

	protected List<Map<String, Object>> convertToLtItem(List<UkOrderItemDto> orderItems)
	{
		List<Map<String, Object>> items = new LinkedList<>();

		for (UkOrderItemDto orderItem : orderItems)
		{
			if (CmStringUtils.isEmpty(orderItem.getMaterialNo()))
			{
				continue;
			}

			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("ITEM", orderItem.getLineNo());
			itemMap.put("MATERIAL", CmStringUtils.trimToEmpty(orderItem.getMaterialNo()).toUpperCase());
			itemMap.put("QTY", orderItem.getAdjustedOrderQty());

			items.add(itemMap);
		}

		return items;
	}

	protected UkSimulationResultDto makeSimulationResult(RfcResponse rfcResponse)
	{
		UkSimulationResultDto simulationResult = new UkSimulationResultDto();

		//Make header info
		makeHeaderInfo(rfcResponse, simulationResult);

		//Make pricing result
		List<UkPricingResultDto> pricingResults = simulationResult.getPricingResults();

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
	protected void makeErrorOrderItems(RfcResponse rfcResponse, UkSimulationResultDto simulationResult)
	{
		Map<String, IDataList> itemMap = new HashMap<>();
		itemMap.put("NON_EXIST", rfcResponse.getTable("LT_NON_EXIST"));
		itemMap.put("WRONG_DIVISION", rfcResponse.getTable("LT_WRONG_DIVISION"));
		itemMap.put("NON_PURCHASE", rfcResponse.getTable("LT_NON_PURCHASE"));

		Iterator<Entry<String, IDataList>> iterator = itemMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<String, IDataList> entry = iterator.next();

			IDataList items = entry.getValue();

			if (CollectionUtils.isNotEmpty(items))
			{
				List<UkOrderItemDto> errorOrderItems = simulationResult.getErrorOrderItems();

				for (int i=0, size=items.size(); i<size; i++)
				{
					DataMap dataMap = (DataMap)items.get(i);

					UkOrderItemDto orderItem = new UkOrderItemDto();
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

	protected void makeHeaderInfo(RfcResponse rfcResponse, UkSimulationResultDto simulationResult)
	{
		/* Make Header Info */
		DataMap mainHeadResult = rfcResponse.getStructure("LS_MAIN_HEAD_RESULT");

		FormatBuilder.with(mainHeadResult).decimalFormat(new String[] {"CREDIT", "NETWR"}, "#,##0.00");
		FormatBuilder.with(mainHeadResult).decimalFormat("TOTAL_WEIGHT", "#,##0.000");

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

	protected void makePricingResult(IDataList dataList, List<UkPricingResultDto> pricingResults)
	{
		for (Object obj : dataList)
		{
			DataMap dataMap = new DataMap(obj);

			UkPricingResultDto pricingResult = new UkPricingResultDto();
			pricingResult.setName(dataMap.getString("NAME"));
			pricingResult.setNetValue(dataMap.getString("NEWR"));
			pricingResult.setCurrency(dataMap.getString("WAERK"));

			pricingResults.add(pricingResult);
		}
	}

	// Make normal order items
	protected void makeNormalOrderItems(RfcResponse rfcResponse, UkSimulationResultDto simulationResult)
	{
		IDataList items = rfcResponse.getTable("LT_ITEM");

		if (CollectionUtils.isNotEmpty(items))
		{
			FormatBuilder.with(items)
				.integerFormat(new String[] {"QTY", "LOT_QTY", "MIN_QTY", "DIUK"})
				.decimalFormat(new String[] {"NET_PRICE", "NET_VALUE", "NETPR"});

			List<UkOrderItemDto> normalOrderItems = new LinkedList<>();

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

				UkOrderItemDto orderItem = new UkOrderItemDto();
				orderItem.setLineNo(dataMap.getString("ITEM"));
				orderItem.setHigherLineNo(dataMap.getString("HILV"));
				orderItem.setMaterialNo(CmStringUtils.defaultIfEmpty(dataMap.getString("MATERIAL_ENT"), dataMap.getString("MATERIAL")));
				orderItem.setSupplyMaterialNo(dataMap.getString("MATERIAL"));
				orderItem.setDescription(dataMap.getString("MATERIAL_TEXT"));
				orderItem.setOrderQty(dataMap.getString("QTY"));
				orderItem.setAvailability(dataMap.getString("AVAILABILITY"));
				orderItem.setMinOrderQty(Integer.toString(minQty));
				orderItem.setLotQty(Integer.toString(lotQty));
				orderItem.setUom(dataMap.getString("UOM"));
				orderItem.setNetPrice(dataMap.getString("NET_PRICE"));
				orderItem.setNetValue(dataMap.getString("NET_VALUE"));
				orderItem.setListPrice(dataMap.getString("NETPR"));
				orderItem.setCostCode(dataMap.getString("KONDM"));
				orderItem.setMessage(dataMap.getString("MSG"));
				orderItem.setVendor(dataMap.getString("LIFNR"));
				orderItem.setPlant(dataMap.getString("PLANT"));
				orderItem.setStockDivuk(dataMap.getString("DIUK"));

				normalOrderItems.add(orderItem);
			}

			simulationResult.setNormalOrderItems(normalOrderItems);
		}
	}
}
