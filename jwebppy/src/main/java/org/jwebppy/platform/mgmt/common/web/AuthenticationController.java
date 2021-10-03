package org.jwebppy.platform.mgmt.common.web;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyType;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyVo;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserPasswordChangeHistoryService;
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
	private CredentialsPolicyService credentialsPolicyService;

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserPasswordChangeHistoryService userPasswordChangeHistoryService;

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
	public Object checkValidPassword(HttpSession session, @RequestParam String password, @RequestParam String newPassword, @RequestParam String confirmPassword)
	{
		String username = CmStringUtils.defaultIfEmpty(session.getAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME), UserAuthenticationUtils.getUsername());

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("TYPE", CredentialsPolicyType.P);

		List<String> messages = new ArrayList<>();

		if (CmStringUtils.isNotEmpty(username))
		{
			if (CmStringUtils.equals(newPassword, confirmPassword))
			{
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
								resultMap = credentialsPolicyService.checkValid(userAccount.getCredentialsPolicy(), CredentialsPolicyType.P, newPassword);
								int result = (Integer)resultMap.get("RESULT");

								if (result == CredentialsPolicyVo.VALID)
								{
									userAccount.setPassword(newPassword);
									userAccount.setFgPasswordLocked(PlatformCommonVo.NO);

									userService.modifyUserAccount(userAccount);

									return resultMap;
								}
								else
								{
									messages = (List<String>)resultMap.get("MESSAGE");
								}
							}
							else
							{
								messages.add("A new password should not be same with your previod one.");
							}
						}
						else
						{
							messages.add("The password you inputed doesn't match your current password.");
						}
					}
					else
					{
						messages.add("The account is not valid.");
					}
				}
				else
				{
					messages.add("There is no account");
				}
			}
			else
			{
				messages.add("Please check the password. The password and confirm password don't match.");
			}
		}
		else
		{
			messages.add("Session has expired. Please try this again.");
			resultMap.put("RESULT", -999);
		}

		resultMap.put("MESSAGE", messages);

		return resultMap;
	}

	@GetMapping("/last_login_info")
	@ResponseBody
	public Object lastLoginInfo(@ModelAttribute("loginHistorySearchDto") LoginHistorySearchDto loginHistorySearch)
	{
		loginHistorySearch.setRowPerPage(2);
		loginHistorySearch.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		loginHistorySearch.setFgResult(PlatformCommonVo.YES);

		List<LoginHistoryDto> loginHistories = loginHistoryService.getPageableLoginHistories(loginHistorySearch);

		if (CollectionUtils.isNotEmpty(loginHistories))
		{
			if (loginHistories.size() == 1)
			{
				return loginHistories.get(0);
			}
			else
			{
				return loginHistories.get(1);
			}
		}

		return null;
	}

	@GetMapping("/password_expiration/check")
	@ResponseBody
	public Object checkPasswordExpiration()
	{
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("expiredDate", "");
		resultMap.put("difference", "-1");

		UserDto user = userService.getUser(UserAuthenticationUtils.getUSeq());

		int pwdValidPeriod = user.getUserAccount().getCredentialsPolicy().getPwdValidPeriod();

		if (pwdValidPeriod > 0)
		{
			UserPasswordChangeHistoryDto inUserPasswordChangeHistory = new UserPasswordChangeHistoryDto();
			inUserPasswordChangeHistory.setUSeq(UserAuthenticationUtils.getUSeq());
			inUserPasswordChangeHistory.setPageNumber(1);
			inUserPasswordChangeHistory.setRowPerPage(1);

			List<UserPasswordChangeHistoryDto> userPasswordChangeHistories = userPasswordChangeHistoryService.getPageableUserPasswordChangeHistories(inUserPasswordChangeHistory);

			String timezone = null;
			ZonedDateTime regDate = null;
			ZonedDateTime exiredDate = null;

			if (CollectionUtils.isNotEmpty(userPasswordChangeHistories))
			{
				UserPasswordChangeHistoryDto userPasswordChangeHistory = userPasswordChangeHistories.get(0);

				timezone = userPasswordChangeHistory.getTimezone();
				regDate = ZonedDateTime.of(userPasswordChangeHistory.getRegDate(),  ZoneId.of(timezone));
				exiredDate = regDate.plusDays(pwdValidPeriod);
			}
			else
			{
				timezone = user.getUserContactInfo().getTimezone();
				regDate = ZonedDateTime.of(user.getRegDate(),  ZoneId.of(timezone));
				exiredDate = regDate.plusDays(pwdValidPeriod);
			}

			resultMap.put("expiredDate", CmDateFormatUtils.format(exiredDate, PlatformCommonVo.DEFAULT_DATE_FORMAT));
			resultMap.put("difference", Long.toString(ChronoUnit.DAYS.between(CmDateTimeUtils.now(timezone), exiredDate)));
		}

		return resultMap;
	}
}