package org.jwebppy;

import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexContoller extends PlatformGeneralController
{
	@RequestMapping("/")
	public Object index(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws UnknownHostException
	{
		if (UserAuthenticationUtils.isAuthenticated())
		{
			return new RedirectView(PortalConfigVo.CONTEXT_PATH + "/forward/entry_point");
		}

		return new RedirectView(PortalConfigVo.FORM_LOGIN_PAGE_URL);
	}

	public static void main(String[] args)
	{
		System.err.println(AES256Cipher.getInstance().encode("divdp"));
		System.err.println(AES256Cipher.getInstance().encode("1111"));


	}
}
