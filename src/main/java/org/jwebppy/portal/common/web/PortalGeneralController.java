package org.jwebppy.portal.common.web;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.GeneralController;

public class PortalGeneralController extends GeneralController
{
	public static final String[] MANAGER_AUTHORITIES = {"DP_DIDO_PARTS_MANAGER", "DP_DIEX_PARTS_MANAGER", "DP_DIEU_PARTS_MANAGER", "DP_DIUK_PARTS_MANAGER"};//각법인 내부사용자

	public boolean isManager()
	{
		return UserAuthenticationUtils.hasRole(MANAGER_AUTHORITIES);
	}
}
