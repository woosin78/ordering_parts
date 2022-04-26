package org.jwebppy.platform.mgmt.sso.web;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso")
public class SsoController extends PlatformGeneralController
{
	private final static Map<String, String[]> SSO_TARGET = new HashMap<>();

	static
	{
		SSO_TARGET.put("gpes", new String[] {"DIV", "DIVORDER", "https://gpes.doosan-iv.com/ssoextra/sso_login_div_order.jsp?p_encryptsso="});
		SSO_TARGET.put("bulletin", new String[] {"DIV", "DIVDOOBIZ", "https://gpes.doosan-iv.com/ssoextra/sso_login_doobiz.jsp?BULLETIN_STS=NEW_BULL&PART_NO=&LAN_CD=EN&p_encryptsso="});
		SSO_TARGET.put("doobiz", new String[] {"DIV", "DOOBIZ"});
	}

	@RequestMapping("/{target}")
	public Object sso(@PathVariable(value = "target") String target, Model model)
	{
        try
		{
        	String[] secret = SSO_TARGET.get(target);

        	model.addAttribute("key", secret[2] + URLEncoder.encode(encrypt(secret[0], secret[1]), "UTF-8"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/" + target;
	}

	/*
	@RequestMapping("/gpes")
	public Object gpes(Model model)
	{
        try
		{
        	model.addAttribute("key", URLEncoder.encode(encrypt("DIV", "DIVORDER"), "UTF-8"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/gpes";
	}

	@RequestMapping("/bulletin")
	public Object bulletin(Model model)
	{
        try
		{
			model.addAttribute("key", URLEncoder.encode(encrypt("DIV", "DIVDOOBIZ"), "UTF-8"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/bulletin";
	}
	*/

	protected String encrypt(String key, String iv)
	{
		try
		{
			return new StringEncrypter(key, iv).encrypt(getUsername().toUpperCase() + "|@" + CmDateFormatUtils.now(IvCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
