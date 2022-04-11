package org.jwebppy.platform.core.service;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;

public abstract class GeneralService
{
	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}
}
