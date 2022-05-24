package org.jwebppy.portal.iv.hq.parts.domestic.order.display.service;

import org.jwebppy.platform.core.cache.CacheHelper;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDisplayService extends PartsDomesticGeneralService
{
	@Autowired
	private CacheHelper cacheHelper;

	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.ORDER_DISPLAY, unless="#result == null")
	public RfcResponse getList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDERLIST");

		rfcRequest
			.field()
				.add("I_LANGU", paramMap.getLangForSap())
			.and()
			.structure("LS_SEARCH").with(paramMap)
				.add(new Object[][] {
					{"KUNNR", paramMap.getCustomerNo()},
					{"VKORG", paramMap.getSalesOrg()},
					{"SPART", paramMap.getDivision()},
					{"KVGR5", paramMap.getCustomerGrp5()}
				})
				.addByKey(new Object[][] {
					{"VBTYP", "docType"},
					{"AUART", "orderType"},
					{"VBELN", "orderNo"},
					{"BSTKD", "poNo"},
					{"MATNR", "orderPartNo"},
					{"FRDATE", "fromDate"},
					{"TODATE", "toDate"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
			.output("LT_SEARCH");

		return simpleRfcTemplate.response(rfcRequest);
	}

	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.ORDER_DISPLAY, unless="#result == null")
	public RfcResponse getView(ErpDataMap paramMap)
	{
		//cacheHelper.evict(PortalCacheConfig.ORDER_DISPLAY);

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDER_LOAD");

		rfcRequest
			.field()
				.add("I_LANGU", paramMap.getLangForSap())
				.addByKey("LV_REF_ORD", "orderNo")
			.and()
			.structure("LS_IMPORT_PORTAL")
				.add(new Object[][] {
					{"DOC_CATEGORY", paramMap.getString("docType", "C")},
					{"COMPLAINT", "N"},
					{"DOC_MODE", "C"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getDownload(ErpDataMap paramMap)
	{
		if (paramMap.isEquals("mode", "Y006"))
		{
			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SD_DOC_PRNT_V");

			rfcRequest
				.field().with(paramMap)
					.addByKey(new Object[][] {
						{"KSCHL", "mode"},
						{"LS_VBELN", "orderNo"}
					})
				.structure("CTL_OPTION")
					.add(new Object[][] {
						{"LANGU", "EN"},
						{"PDF_VIEW", "X"}
					})
				.and()
				.output("T_PDF");

			return simpleRfcTemplate.response(rfcRequest).getTable("T_PDF");
		}
		else
		{
			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_COMPLAIN_PRINT");

			rfcRequest
				.field().with(paramMap)
					.add(new Object[][] {
						{"I_USERID", paramMap.getUsername()},
						{"I_LANGU", paramMap.getLangForSap()}
					})
					.addByKey(new Object[][] {
						{"ID", "mode"},
						{"LV_REF_ORD", "orderNo"}
					})
					.and()
					.output("LT_TAB");

			return simpleRfcTemplate.response(rfcRequest).getTable("LT_TAB");
		}
	}
}
