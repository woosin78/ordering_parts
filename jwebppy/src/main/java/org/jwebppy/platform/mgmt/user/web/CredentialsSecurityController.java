package org.jwebppy.platform.mgmt.user.web;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/user/credentials/security")
public class CredentialsSecurityController extends UserGeneralController
{

}
