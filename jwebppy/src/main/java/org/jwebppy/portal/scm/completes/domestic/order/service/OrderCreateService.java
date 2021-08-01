package org.jwebppy.portal.scm.completes.domestic.order.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.portal.scm.completes.CompletesErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCreateService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public DataList getModel(CompletesErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_MODEL_LIST");

		rfcRequest.addField("I_BZIRK", paramMap.getSalesDistrict());
		rfcRequest.addField("I_DUPLICATE", "X");
		rfcRequest.addField("I_KUNNR", paramMap.getCustomerNo());
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_SPART", paramMap.getDivision());
		rfcRequest.addField("I_VKORG", paramMap.getSalesOrg());
		rfcRequest.addField("I_VTWEG", paramMap.getDistChannel());

		return simpleRfcTemplate.response(rfcRequest).getTable("T_LIST");
	}
}
