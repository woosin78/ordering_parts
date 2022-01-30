package org.jwebppy.portal.iv.mgmt.account.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountMgmtService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public DataList getMappingList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_DEALER_MAPPING_LIST");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"I_BNAME", "username"},
					{"I_KUNNR", "dealerCode"}
				})
			.and()
			.table("T_VKORG").with(paramMap)
				.addByKey("T_VKORG", "salesAreaList")
			.output("T_LIST");

		return simpleRfcTemplate.response(rfcRequest).getTable("T_LIST");
	}
}
