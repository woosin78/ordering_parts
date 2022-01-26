package org.jwebppy.portal.iv.hq.parts.domestic.info.service;

import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PartInfoService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getItemSubList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ITEM_SUB_LIST");

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLang()},
					{"I_USERID", paramMap.getUsername()},
					{"I_MATNR", "partNo"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getPartsStandardM(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_PARTS_STANDARD");

		rfcRequest.with(paramMap)
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"I_LANGU", paramMap.getLang()},
				{"I_USERID", paramMap.getUsername()},
				{"I_KVGR5", paramMap.getCustomerGrp5()},
				{"I_REQTY", "reqQty"}
			})
			.and()
			.table("T_MATNR")
				.add("MATNR", "partNo");


		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getPartList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MARA");

		rfcRequest.with(paramMap)
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"ILANGU", paramMap.getLang()},
				{"I_USERID", paramMap.getUsername()},
				{"MAKTX", "partDesc"},
				{"MATNR", "partNo"}
			});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getSubItemList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ITEM_SUB_LIST");

		rfcRequest.with(paramMap)
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"I_LANGU", paramMap.getLang()},
				{"I_USERID", paramMap.getUsername()},
				{"I_MATNR", "partNo"}
			});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAppliedModelList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_APP_MODEL_LIST");

		rfcRequest.with(paramMap)
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"I_LANGU", paramMap.getLang()},
				{"I_USERID", paramMap.getUsername()},
				{"I_KVGR5", "productType"},
				{"I_MATNR", "partNo"},
				{"I_MVGR3", "productType"}
			});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAlternativeList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_MULTI_SUBST");

		rfcRequest.with(paramMap)
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"I_LANGU", paramMap.getLang()},
				{"I_USERID", paramMap.getUsername()},
				{"I_MATNR", "partNo"}
			});

		return simpleRfcTemplate.response(rfcRequest);
	}

	@Cacheable(value = CacheConfig.PARTS_INFO_AUTOCOMPLETE, key = "#paramMap", unless="#result == null")
	public DataList getSimplePartsInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY2");

		rfcRequest
			.field(new Object[][] {
				{"I_BGTYP", "P"},//상수
				{"I_LANGU", paramMap.getLang()},
				{"I_USERID", paramMap.getUsername()}
			})
			.and()
			.table("LT_ITEM")
				.add("MATERIAL", paramMap.getString("partNo"))
			.and()
			.output("LT_ITEM");

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");
	}
}
