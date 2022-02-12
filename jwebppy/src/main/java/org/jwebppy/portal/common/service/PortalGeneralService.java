package org.jwebppy.portal.common.service;

import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class PortalGeneralService
{
	@Autowired
	protected SimpleRfcTemplate simpleRfcTemplate;

	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}
}
