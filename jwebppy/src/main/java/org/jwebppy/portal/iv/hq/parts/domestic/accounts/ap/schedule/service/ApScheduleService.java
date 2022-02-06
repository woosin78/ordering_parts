package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ap.schedule.service;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApScheduleService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_CLEARED_AR_BY_DAY");

		Map<String, Object> budatMap = new HashMap<>();
		budatMap.put("SIGN", "I");
		budatMap.put("OPTION", "BT");
		budatMap.put("LOW", paramMap.get("fromDate"));
		budatMap.put("HIGH", paramMap.get("toDate"));

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_BUKRS", paramMap.getCorpNo()},
					{"I_LOCAL", "X"},
					{"I_USERID", paramMap.getUsername()}
				})
			.and()
			.table("I_BUDAT")
				.add(budatMap)
			.output(new String[] {"LS_9189", "I_LIST"});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
