package org.jwebppy.platform.mgmt.sso.web;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso/out/gpes")
public class GpesSsoOutController extends SsoController
{
	private final static Map<String, String[]> TARGET = new HashMap<>();
	private final String PRD_URL = "https://gpes.doosan-iv.com";
	private final String DEV_URL = "https://gpes.doosan-iv.com";

	static
	{
		TARGET.put("sso", new String[] {"DIV", "DIVORDER", "/ssoextra/sso_login_div_order.jsp?p_encryptsso="});
		TARGET.put("bulletin", new String[] {"DIV", "DIVDOOBIZ", "/ssoextra/sso_login_doobiz.jsp?BULLETIN_STS=NEW_BULL&PART_NO=&LAN_CD=EN&p_encryptsso="});
	}

	@RequestMapping
	public Object sso(@RequestParam(value = "target", required = false, defaultValue = "sso") String target, Model model)
	{
        try
		{
        	String url = isProduction() ? PRD_URL : DEV_URL;

        	String[] secret = TARGET.get(target);

        	model.addAttribute("url", url + secret[2] + URLEncoder.encode(encrypt(secret[0], secret[1], "|@"), "UTF-8"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/out/gpes/sso";
	}
}
