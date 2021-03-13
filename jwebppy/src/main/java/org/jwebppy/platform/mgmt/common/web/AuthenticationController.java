package org.jwebppy.platform.mgmt.common.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/common/authentication")
public class AuthenticationController extends PlatformGeneralController
{
	@Autowired
	private Environment environment;

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private UserService userService;

	@RequestMapping("/login_form")
	public String loginForm(Model model)
	{
		model.addAttribute("key", environment.getProperty("sso.ad.key"));
		model.addAttribute("token", environment.getProperty("sso.ad.domainToken"));
		model.addAttribute("secret", environment.getProperty("sso.ad.domainSecret"));

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/check_password_expired")
	@ResponseBody
	public Object checkPasswordExpired()
	{
		if (UserAuthenticationUtils.isLogin())
		{
			return userService.getUserByUsername(getUsername()).getUserAccount().getFgPasswordLocked();
		}

		return PlatformCommonVo.NO;
	}

	@GetMapping("/last_login_info")
	@ResponseBody
	public Object lastLoginHistory(@ModelAttribute("loginHistorySearchDto") LoginHistorySearchDto loginHistorySearch)
	{
		loginHistorySearch.setRowPerPage(2);
		loginHistorySearch.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		loginHistorySearch.setFgResult(PlatformCommonVo.YES);

		List<LoginHistoryDto> loginHistories = loginHistoryService.getPageableLoginHistories(loginHistorySearch);

		if (CollectionUtils.isNotEmpty(loginHistories) && loginHistories.size() > 1)
		{
			return loginHistories.get(1);
		}

		return null;
	}
}