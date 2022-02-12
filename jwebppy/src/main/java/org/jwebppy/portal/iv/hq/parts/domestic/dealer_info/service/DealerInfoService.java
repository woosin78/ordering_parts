package org.jwebppy.portal.iv.hq.parts.domestic.dealer_info.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.stereotype.Service;

@Service
public class DealerInfoService extends PartsDomesticGeneralService
{
	public RfcResponse getDealerInfo(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_CUSTOMER_DISPLAY");

		rfcRequest
			.field("I_USERID", paramMap.getUsername())
			.and()
			.structure("I_ORG")
				.add(new Object[][] {
					{"VKORG", paramMap.getSalesOrg()},
					{"VTWEG", paramMap.getDistChannel()},
					{"SPART", paramMap.getDivision()}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getDealerInfoByDealerCode(String dealerCode)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ID_GEN");

		rfcRequest.addField("I_KUNNR", dealerCode);
		rfcRequest.addOutputParameter("O_KNA");

		return simpleRfcTemplate.response(rfcRequest);
	}
}
