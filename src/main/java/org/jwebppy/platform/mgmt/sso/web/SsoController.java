package org.jwebppy.platform.mgmt.sso.web;

import java.net.URLEncoder;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso")
public class SsoController extends PlatformGeneralController
{
	@RequestMapping("/gpes")
	public Object gpes(Model model)
	{
        try
		{
        	String time = CmDateFormatUtils.now(IvCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);

        	StringEncrypter stringEncrypter = new StringEncrypter("DIV", "DIVORDER");
			String key = stringEncrypter.encrypt(getUsername().toUpperCase() + "|@" + time);

			model.addAttribute("key", URLEncoder.encode(key, "UTF-8"));
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
        	String time = CmDateFormatUtils.now(IvCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);

        	StringEncrypter stringEncrypter = new StringEncrypter("DIV", "DIVDOOBIZ");
			String key = stringEncrypter.encrypt(getUsername().toUpperCase() + "|@" + time);

			model.addAttribute("key", URLEncoder.encode(key, "UTF-8"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/bulletin";
	}
}
