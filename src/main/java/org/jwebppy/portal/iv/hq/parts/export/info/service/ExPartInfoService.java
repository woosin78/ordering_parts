package org.jwebppy.portal.iv.hq.parts.export.info.service;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExPartInfoService extends PartsExportGeneralService
{
	public RfcResponse getPartInfo(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_PARTS_STANDARD");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_KVGR5", "20"},
					{"I_REQTY", paramMap.getString("reqQty")}
			})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
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
					{"ILANGU", paramMap.getLangForSap()},
					{"MATNR", paramMap.getString("partNo").toUpperCase()},
					{"MAKTX", paramMap.getString("partDesc")}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getSubItemList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ITEM_SUB_LIST");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_MATNR", paramMap.getString("partNo").toUpperCase()}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAppliedModelList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_APP_MODEL_LIST");

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_MATNR", paramMap.getString("partNo").toUpperCase()}
				})
				.addByKey(new Object[][] {
					{"I_KVGR5", "productType"},
					{"I_MVGR3", "productType"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAlternativeList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MULTI_SUBST");

		rfcRequest
			.field(new Object[][] {
				{"I_LANGU", paramMap.getLangForSap()},
				{"I_MATNR", paramMap.getString("partNo").toUpperCase()}
			})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

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
			.output("LT_ITEM");

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}
}
