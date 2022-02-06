package org.jwebppy.portal.iv.common.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.service.PortalGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IvGeneralService extends PortalGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getErpUserInfo(String username, String corpCode, String moduleCode)
	{
		if (CmStringUtils.isNotEmpty(username))
		{
			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USER_PARAM");

			rfcRequest.
				field(new Object[][] {
					{"I_USERID", CmStringUtils.upperCase(username)},
					{"I_BGCON", corpCode},
					{"I_BGTYP", moduleCode}
				});

			return simpleRfcTemplate.response(rfcRequest);
		}

		return null;
	}
}
