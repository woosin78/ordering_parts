package org.jwebppy.platform.security.authentication.web;

import org.jwebppy.platform.PlatformGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/security/session")
public class SessionController extends PlatformGeneralController
{
	@GetMapping("/refresh")
	@ResponseBody
	public Object expend()
	{
		return EMPTY_RETURN_VALUE;
	}
}
