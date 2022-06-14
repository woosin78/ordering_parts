package org.jwebppy.platform.mgmt.sso.web.doobiz;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.mgmt.sso.web.SsoController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso/out/doobiz")
public class DoobizSsoOutController extends SsoController
{
	private final String KEY = "Infracore";
	private final String IV = "Doosan";

	@RequestMapping
	public Object sso(Model model, WebRequest webRequest)
	{
        try
		{
        	model.addAttribute("url", getDoobizDomain() + "/irj/portal");
        	model.addAttribute("token", encrypt(KEY, IV, ":"));
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}

		return "/portal/iv/sso/out/doobiz/sso";
	}
}
