package org.jwebppy.portal.iv.hq.parts.domestic.info.service;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class PartInfoService extends PartsDomesticGeneralService
{
	public RfcResponse getPartInfo(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_PARTS_STANDARD");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()},
					{"I_KVGR5", "20"},
					{"I_REQTY", paramMap.getString("reqQty")}
			})
			.and()
			.table("T_MATNR")
				.add("MATNR", paramMap.getString("partNo").toUpperCase());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getPartList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MARA");

		rfcRequest
			.field(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"ILANGU", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()},
					{"MATNR", paramMap.getString("partNo").toUpperCase()},
					{"MAKTX", paramMap.getString("partDesc")}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getSubItemList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ITEM_SUB_LIST");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()},
					{"I_MATNR", paramMap.getString("partNo").toUpperCase()}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAppliedModelList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_APP_MODEL_LIST");

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_USERID", paramMap.getUsername()},
					{"I_MATNR", paramMap.getString("partNo").toUpperCase()}
				})
				.addByKey(new Object[][] {
					{"I_KVGR5", "productType"},
					{"I_MVGR3", "productType"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAlternativeList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MULTI_SUBST");

		rfcRequest
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"I_LANGU", paramMap.getLangForSap()},
				{"I_USERID", paramMap.getUsername()},
				{"I_MATNR", paramMap.getString("partNo").toUpperCase()}
			});

		return simpleRfcTemplate.response(rfcRequest);
	}

	//@Cacheable(cacheManager = "portalCacheManager", value = PortalCacheConfig.PARTS_INFO_AUTOCOMPLETE, unless="#result == null")
	public DataList getSimplePartInfo(ErpDataMap paramMap)
	{
		Map<String, Object> partMap = new HashMap<>();
		partMap.put("MATERIAL", paramMap.get("partNo"));
		partMap.put("MATERIAL_TEXT", paramMap.get("partDesc"));

		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY2");

		rfcRequest
			.structure("I_INPUT")
				.add(new String[][] {
					{"STATUS", "X"},
					{"VKORG", paramMap.getSalesOrg()},
					{"VTWEG", paramMap.getDistChannel()},
					{"SPART", paramMap.getDivision()}
				})
			.and()
			.table("LT_ITEM")
				.add(partMap)
			.and()
			.output("LT_ITEM");

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}
}
