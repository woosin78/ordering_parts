package org.jwebppy.portal.iv.hq.parts.domestic.order.status.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.common.PortalConfigVo;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService extends PartsDomesticGeneralService
{
	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.ORDER_STATUS, unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		String fromDate = paramMap.getString("fromDate");
		String toDate = paramMap.getString("toDate");

		if (paramMap.isNotEmptyValue("orderNo") || paramMap.isNotEmptyValue("poNo"))
		{
        	fromDate = "20080101";
        	toDate = CmDateFormatUtils.unlimitDate(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD);
		}

        RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_OR_STATUS_INFO");

        rfcRequest
        	.field().with(paramMap)
        		.add(new Object[][] {
        			{"I_FDATE", fromDate},
        			{"I_TDATE", toDate}
        		})
        		.addByKey(new Object[][] {
        			{"I_AUART", "orderType"},
        			{"I_VBELN", "orderNo"},
        			{"I_BSTKD", "poNo"},
        			{"I_MATNR", "orderPartNo"}
        		})
    		.and()
    		.structure("I_INPUT")
    			.add(SimpleRfcMakeParameterUtils.me(paramMap))
    		.output("T_HEADER");

		return simpleRfcTemplate.response(rfcRequest);
	}

	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.ORDER_STATUS, unless="#result == null")
	public RfcResponse getView(ErpDataMap paramMap)
	{
		String[] orderNos = CmStringUtils.split(CmStringUtils.strip(paramMap.getString("orderNo"), PortalConfigVo.DELIMITER), PortalConfigVo.DELIMITER);

		if (ArrayUtils.isNotEmpty(orderNos))
		{
			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ST_INFO_DETAIL");

			rfcRequest.
				field().with(paramMap)
					.add(new Object[][] {
						{"I_BGTYP", "P"},
						{"I_USERID", paramMap.getUsername()},
						{"I_DROP", "0"},
						{"I_FDATE", "20080101"},
						{"I_TDATE", CmDateFormatUtils.unlimitDate(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD)},
						{"I_STATUS", "0"}
					});

			if (orderNos.length > 1)
			{
				List<Map<String, Object>> orderNoList = new LinkedList<>();

				for (String orderNo: orderNos)
				{
					Map<String, Object> itemMap = new HashMap<>();
					itemMap.put("VBELN", orderNo);

					orderNoList.add(itemMap);
				}

				rfcRequest.addTable("LT_VBELN", orderNoList);
			}
			else
			{
				rfcRequest.addField("I_VBELN", orderNos[0]);
			}

			return simpleRfcTemplate.response(rfcRequest);
		}

		return null;
	}
}
