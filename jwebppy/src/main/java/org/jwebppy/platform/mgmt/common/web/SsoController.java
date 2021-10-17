package org.jwebppy.platform.mgmt.common.web;

import org.jwebppy.platform.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/common/sso")
public class SsoController extends PlatformGeneralController
{
}
