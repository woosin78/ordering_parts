package org.jwebppy.portal.iv.eu.parts.common.service;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.eu.common.service.EuGeneralService;
import org.springframework.stereotype.Service;

@Service
public class EuPartsGeneralService extends EuGeneralService
{
	public RfcResponse getErpUserInfo(String username)
	{
		return super.getErpUserInfo(username, "7", "P");
	}

	public RfcResponse getErpUserInfo()
	{
		return this.getErpUserInfo(UserAuthenticationUtils.getUsername());
	}
}
