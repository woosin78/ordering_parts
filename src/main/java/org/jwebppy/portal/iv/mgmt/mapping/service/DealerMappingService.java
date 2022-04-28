package org.jwebppy.portal.iv.mgmt.mapping.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DealerMappingService extends IvGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_SR_DEALER");

		rfcRequest.
			field()
				.add(new Object[][] {
					{"I_USERID", paramMap.getUsername()},
					{"I_KUNNR", paramMap.getString("name")},
					{"I_NAME", paramMap.getString("name")}
				})
			.structure("I_ORG")
				.add("VKORG", paramMap.getSalesOrg());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse changeMapping(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_CHANGE_USER_TO_ANOTHER");

		rfcRequest.
			field()
				.add(new Object[][] {
					{"I_BNAME_FROM", paramMap.getString("fromUsername").toUpperCase()},
					{"I_BNAME_TO", paramMap.getUsername()}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
