package org.jwebppy.platform.mgmt.common.web;

import org.jwebppy.platform.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/common/session")
public class SessionController extends PlatformGeneralController
{
	@GetMapping("/refresh")
	@ResponseBody
	public Object expend()
	{
		return EMPTY_RETURN_VALUE;
	}
}
