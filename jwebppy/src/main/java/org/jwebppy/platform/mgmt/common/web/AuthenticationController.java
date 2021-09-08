package org.jwebppy.platform.mgmt.common.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.FORM_AUTHENTICATION_PATH)
public class AuthenticationController extends PlatformGeneralController
{
	//@Autowired
	//private Environment environment;

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@RequestMapping(PlatformConfigVo.FORM_LOGIN_PAGE_PATH)
	public String login(Model model)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping(PlatformConfigVo.FORM_PASSWORD_CHANGE_PAGE_PATH)
	public Object changePassword()
	{
		return DEFAULT_VIEW_URL;
	}

	@PostMapping(PlatformConfigVo.FORM_PASSWORD_CHANGE_PROCESSING_PATH)
	@ResponseBody
	public Object checkChangePassword(HttpSession session, @RequestParam String password, @RequestParam String newPassword, @RequestParam String confirmPassword)
	{
		if (CmStringUtils.equals(newPassword, confirmPassword))
		{
			String username = CmStringUtils.trimToEmpty(session.getAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME));

			UserDto user = userService.getUserByUsername(username);

			//사용자 정보가 존재하는지 체크
			if (user != null)
			{
				UserAccountDto userAccount = user.getUserAccount();

				//계정이 유효한 상태인지 체크
				if (userAccount.isValidPeriod() && !userAccount.isAccountLocked())
				{
					//현재 비밀번호 체크
					if (passwordEncoder.matches(password, userAccount.getPassword()))
					{
						//이전 비밀번호와 신규 비밀번호가 동일한지 체크
						if (!passwordEncoder.matches(newPassword, userAccount.getPassword()))
						{
							userAccount.setPassword(newPassword);
							userAccount.setFgPasswordLocked(PlatformCommonVo.NO);

							userService.modifyUserAccount(userAccount);

							return PlatformCommonVo.OK;
						}
					}
				}
			}
		}

		return PlatformCommonVo.FAIL;
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