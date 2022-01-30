package org.jwebppy.portal.iv.hq.parts.common.service;

import org.jwebppy.portal.iv.hq.common.service.HqGeneralService;
import org.springframework.stereotype.Service;

@Service
public class DealerInfoService extends HqGeneralService
{
//	@Autowired
//	private SimpleRfcTemplate simpleRfcTemplate;
//
//	public RfcResponse getDealerInfo(ErpDataMap paramMap)
//	{
//		RfcRequest rfcRequest = new RfcRequest("Z_EP_CUSTOMER_DISPLAY");
//
//		rfcRequest.addField("I_USERID", paramMap.getUsername());
//		rfcRequest.addStructure("I_ORG", "SPART", paramMap.getDivision());
//		rfcRequest.addStructure("I_ORG", "VKORG", paramMap.getSalesOrg());
//		rfcRequest.addStructure("I_ORG", "VTWEG", paramMap.getDistChannel());
//
//		return simpleRfcTemplate.response(rfcRequest);
//	}
//
//	public RfcResponse getDealerInfoByDealerCode(String dealerCode)
//	{
//		RfcRequest rfcRequest = new RfcRequest("Z_EP_ID_GEN");
//
//		rfcRequest.addField("I_KUNNR", dealerCode);
//
//		return simpleRfcTemplate.response(rfcRequest);
//	}
}
