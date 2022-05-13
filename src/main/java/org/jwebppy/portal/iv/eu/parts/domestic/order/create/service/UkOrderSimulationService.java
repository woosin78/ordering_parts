package org.jwebppy.portal.iv.eu.parts.domestic.order.create.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.IDataList;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto.EuOrderDto;
import org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto.EuOrderItemDto;
import org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto.EuSimulationResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UkOrderSimulationService extends EuOrderSimulationService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@Override
	public EuSimulationResultDto simulation(EuOrderDto order)
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

			for (EuOrderItemDto orderItem : order.getOrderItems())
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

		return new EuSimulationResultDto();
	}

	// Make normal order items
	@Override
	protected void makeNormalOrderItems(RfcResponse rfcResponse, EuSimulationResultDto simulationResult)
	{
		IDataList items = rfcResponse.getTable("LT_ITEM");

		if (CollectionUtils.isNotEmpty(items))
		{
			FormatBuilder.with(items)
				.integerFormat(new String[] {"QTY", "LOT_QTY", "MIN_QTY", "DIUK"})
				.decimalFormat(new String[] {"NET_PRICE", "NET_VALUE", "NETPR"});

			List<EuOrderItemDto> normalOrderItems = new LinkedList<>();

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

				EuOrderItemDto orderItem = new EuOrderItemDto();
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
