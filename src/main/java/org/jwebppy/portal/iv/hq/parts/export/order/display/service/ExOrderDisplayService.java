package org.jwebppy.portal.iv.hq.parts.export.order.display.service;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExOrderDisplayService extends PartsExportGeneralService
{
	@Cacheable(value = PortalCacheConfig.IVEX_ORDER_DISPLAY, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDERLIST");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"COMPLAINT", paramMap.getString("complaint")}
				})
			.and()
			.structure("LS_SEARCH").with(paramMap)
				.add(new Object[][] {
					{"KUNNR", paramMap.getCustomerNo()},
					{"VKORG", paramMap.getSalesOrg()},
					{"SPART", paramMap.getDivision()},
					{"KVGR5", "20"},
					{"MATNR", paramMap.getString("orderPartNo").toUpperCase()}
				})
				.addByKey(new Object[][] {
					{"VBTYP", "docType"},
					{"AUART", "orderType"},
					{"VBELN", "orderNo"},
					{"BSTKD", "poNo"},
					{"FRDATE", "fromDate"},
					{"TODATE", "toDate"},
					{"RFGSK", "status"}
				})
			.and()
			.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
			.output("LT_SEARCH");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDER_LOAD");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"LV_REF_ORD", paramMap.getString("orderNo")}
				})
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
