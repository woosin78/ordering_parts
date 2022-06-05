package org.jwebppy.portal.iv.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class IvPartsCommonService extends IvGeneralService
{
	@Cacheable(cacheManager = "portalCacheManager", value = PortalCacheConfig.PARTS_INFO_AUTOCOMPLETE, unless="#result == null")
	public DataList getPartsInfo(Map<String, String> paramMap)
	{
		Map<String, Object> partMap = new HashMap<>();
		partMap.put("MATERIAL", paramMap.get("partNo"));
		partMap.put("MATERIAL_TEXT", paramMap.get("partDesc"));

		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY2");

		rfcRequest
			.structure("I_INPUT")
				.add(new String[][] {
					{"STATUS", "X"},
					{"VKORG", paramMap.get("salesOrg")},
					{"VTWEG", paramMap.get("distChannel")},
					{"SPART", paramMap.get("division")}
				})
			.and()
			.table("LT_ITEM")
				.add(partMap)
			.output("LT_ITEM");

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}

	@Cacheable(cacheManager = "portalCacheManager", value = PortalCacheConfig.DEALER_INFO_AUTOCOMPLETE, unless="#result == null")
	public List<DataMap> getDealers(Map<String, String> paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_DEALER_LIST");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_VTWEG", paramMap.get("distChannel")},
					{"I_SPART", paramMap.get("division")}
				})
			.and()
			.table("T_VKORG")
				.add("VKORG", paramMap.get("salesOrg"))
			.output("T_LIST");

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		DataList dataList = rfcResponse.getTable("T_LIST");

		if (CollectionUtils.isNotEmpty(dataList))
		{
			String name = CmStringUtils.trimToEmpty(paramMap.get("name"));
			String dealerCode = CmStringUtils.trimToEmpty(paramMap.get("dealerCode"));

			List<DataMap> resultList = new ArrayList<>();

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				if (CmStringUtils.indexOfIgnoreCaseAndEmpty(dataMap.getString("NAME1"), name) > -1 || CmStringUtils.indexOfIgnoreCaseAndEmpty(dataMap.getString("KUNNR"), dealerCode) > -1)
				{
					resultList.add(dataMap);
				}
			}

			return resultList;
		}

		return Collections.emptyList();
	}
}
