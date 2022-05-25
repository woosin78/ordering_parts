package org.jwebppy.platform.core.security.authentication.web;

import org.jwebppy.platform.core.web.GeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/sso")
public class AdAuthenticationController extends GeneralController
{
	/*
	 * /sso/inbound/** 는 로그인 없이 접근 가능
	 */
	@RequestMapping("/inbound/ad")
	@ResponseBody
	public Object ad(WebRequest webRequest)
	{
		return webRequest.getHeader("DOOSAN_GATEWAY_TOKEN");
	}
}
