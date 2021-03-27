package org.jwebppy.portal;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;

public abstract class PortalGeneralService
{
	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}
}
