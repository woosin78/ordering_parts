package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ar.clear_amount.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ClearAmountService extends PartsDomesticGeneralService
{
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_AS_SALE2");

		rfcRequest
			.field()
				.add(new String[][] {
						{"I_USERID", paramMap.getUsername()},
						{"I_BG", "F"},
						{"I_COMP", paramMap.getString("fgComplete")},
						{"I_FRDT", paramMap.getString("yearMonth")}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
