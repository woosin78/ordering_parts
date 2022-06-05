package org.jwebppy.portal.iv.hq.parts.export.claim.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExClaimDisplayService extends PartsExportGeneralService
{
	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.CLAIM_DISPLAY, unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDERLIST");

		rfcRequest.with(paramMap)
			.field()
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()},
					{"COMPLAINT", "Y"}//상수
				})
			.and()
			.structure("LS_SEARCH").with(paramMap)
				.add("VBTYP", "A")
				.addByKey(new Object[][] {
					{"VBELN", "claimNo"},
					{"VGBEL", "referenceNo"},
					{"MATNR", "partNo"},
					{"FRDATE", "fromDate"},
					{"TODATE", "toDate"}
				})
			.and()
			.output("LT_SEARCH2");

		return simpleRfcTemplate.response(rfcRequest);
	}

	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.CLAIM_DISPLAY, unless="#result == null")
	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDER_LOAD");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"LV_REF_ORD", paramMap.get("orderNo")}
				})
			.and()
			.structure("LS_IMPORT_PORTAL")
				.add("DOC_CATEGORY", paramMap.get("docType"))
			.and()
			.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	//@Cacheable(cacheManager = "portalCacheManager", keyGenerator = "portalCacheKeyGenerator", value = PortalCacheConfig.CLAIM_REASON, unless="#result == null")
	public DataList getClaimReasonList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_COMPLAIN_REASON");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_LANG", paramMap.getLangForSap()},
					{"LS_ZZCMPCD", paramMap.get("reason1")}
				})
			.and()
			.output("LT_VALUESET");

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_VALUESET");
	}

	public RfcResponse getDocument(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_DOWNLOAD");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"LV_REF_ORD", "orderNo"},
					{"DOCU_ITEM", "docuItem"},
					{"INT_NO", "intNo"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAttachment(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_DOWNLOAD");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"LV_REF_ORD", "orderNo"},
					{"DOCU_ITEM", "docuItem"},
					{"INT_NO", "intNo"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
