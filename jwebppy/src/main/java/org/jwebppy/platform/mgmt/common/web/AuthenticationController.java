package org.jwebppy.platform.mgmt.common.web;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.FORM_AUTHENTICATION_PATH)
public class AuthenticationController extends PlatformGeneralController
{
	@RequestMapping(PlatformConfigVo.FORM_LOGIN_PAGE_PATH)
	public String login(Model model)
	{
		return DEFAULT_VIEW_URL;
	}
}