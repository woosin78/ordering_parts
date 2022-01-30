package org.jwebppy.portal.iv.common.service;

import org.jwebppy.portal.common.service.PortalGeneralService;
import org.springframework.stereotype.Service;

@Service
public class IvGeneralService extends PortalGeneralService
{
//	@Autowired
//	private SimpleRfcTemplate simpleRfcTemplate;

	/*
	public RfcResponse getErpUserInfo(String username)
	{
		if (CmStringUtils.isNotEmpty(username))
		{
			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USER_PARAM");

			rfcRequest.addField("I_USERID", CmStringUtils.upperCase(username));
			rfcRequest.addField("I_BGCON", "7");
			rfcRequest.addField("I_BGTYP", "P");

			return simpleRfcTemplate.response(rfcRequest);
		}

		return null;
	}
	*/
}
