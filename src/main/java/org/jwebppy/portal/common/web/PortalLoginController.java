package org.jwebppy.portal.common.web;

import org.jwebppy.platform.mgmt.common.web.AuthenticationController;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PortalConfigVo.FORM_AUTHENTICATION_PATH)
public class PortalLoginController extends AuthenticationController
{
	@Autowired
	private Environment environment;

	@Override
	@RequestMapping(PortalConfigVo.FORM_LOGIN_PAGE_PATH)
	public String login(Model model)
	{
		model.addAttribute("key", environment.getProperty("sso.ad.key"));
		model.addAttribute("token", environment.getProperty("sso.ad.domainToken"));
		model.addAttribute("secret", environment.getProperty("sso.ad.domainSecret"));
		model.addAttribute("gateway", environment.getProperty("sso.ad.platform-gateway-host"));

		return DEFAULT_VIEW_URL;
	}
}
