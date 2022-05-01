package org.jwebppy.platform.mgmt.sso.web.doobiz;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.mgmt.sso.web.SsoController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso/in/doobiz")
public class DoobizSsoInController extends SsoController
{
	@RequestMapping
	public Object in(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return "/portal/iv/sso/in/doobiz/sso";
	}
}
