package org.jwebppy.platform.mgmt.sso.web;

import java.net.URLEncoder;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso/doobiz")
public class DoobizSsoController extends SsoController
{
	private final String KEY = "Infracore";
	private final String IV = "Doosan";
	private final String PRD_URL = "https://doobiz.doosan-iv.com/irj/portal";
	private final String DEV_URL = "https://doobiz-edu.doosan-iv.com/irj/portal";

	@RequestMapping
	public Object out(Model model)
	{
        try
		{
        	String url = isProduction() ? PRD_URL : DEV_URL;

        	model.addAttribute("url", url);
        	model.addAttribute("token", URLEncoder.encode(encrypt(KEY, IV, ":"), "UTF-8"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/doobiz";
	}
}
