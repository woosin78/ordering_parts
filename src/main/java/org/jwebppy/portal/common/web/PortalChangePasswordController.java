package org.jwebppy.portal.common.web;

import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PortalConfigVo.FORM_AUTHENTICATION_PATH)
public class PortalChangePasswordController extends PortalGeneralController
{
	@RequestMapping(PortalConfigVo.FORM_PASSWORD_CHANGE_PAGE_PATH)
	public Object changePassword()
	{
		return DEFAULT_VIEW_URL;
	}
}
