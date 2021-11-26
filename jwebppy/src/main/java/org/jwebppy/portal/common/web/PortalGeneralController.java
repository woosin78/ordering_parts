package org.jwebppy.portal.common.web;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.GeneralController;

public class PortalGeneralController extends GeneralController
{
	@Override
	protected Integer getUSeq()
	{
		return UserAuthenticationUtils.getUSeq();
	}

	@Override
	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}
}
