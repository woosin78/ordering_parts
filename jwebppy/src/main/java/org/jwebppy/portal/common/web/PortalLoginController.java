package org.jwebppy.portal.common.web;

import org.jwebppy.platform.mgmt.common.web.AuthenticationController;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PortalConfigVo.FORM_AUTHENTICATION_PATH)
public class PortalLoginController extends AuthenticationController
{
	@Override
	@RequestMapping(PortalConfigVo.FORM_LOGIN_PAGE_PATH)
	public String login(Model model)
	{
		return DEFAULT_VIEW_URL;
	}
}
