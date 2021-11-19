package org.jwebppy.portal.common.service;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;

public abstract class PortalGeneralService
{
	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}
}
