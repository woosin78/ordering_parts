package org.jwebppy.portal.dbkr.scm.parts.domestic.order;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public DataList getOrderType(ErpDataMap paramMap)
	{
		String docType = CmStringUtils.defaultString(paramMap.get("pDocType"), CmStringUtils.defaultString(paramMap.get("docType"), "C"));

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_ORDERTYPE2");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_STATUS", "X");
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_VBTYP", docType);

		DataList dataList = simpleRfcTemplate.response(rfcRequest).getTable("T_ZSSV0002");

		if ("C".equals(docType))
		{
			if (CmStringUtils.equals(paramMap.get("from"), "MERCHANDIZE"))
			{
				DataList tmpOrderTypes = new DataList();

				for (Object orderType : dataList)
				{
					DataMap orderTypeMap = new DataMap(orderType);

					if (orderTypeMap.isEquals("ZZAUKON", "STOCK ORDER"))
					{
						tmpOrderTypes.add(orderTypeMap);
					}
				}

				return tmpOrderTypes;
			}

			return dataList;
		}

		DataList tmpOrderTypes = new DataList();

		for (Object orderType : dataList)
		{
			DataMap orderTypeMap = new DataMap(orderType);

			if (orderTypeMap.isEquals("AUART", "YDIR") || orderTypeMap.isEquals("AUART", "YEIR"))
			{
				continue;
			}

			tmpOrderTypes.add(orderTypeMap);
		}

		return tmpOrderTypes;
	}
}