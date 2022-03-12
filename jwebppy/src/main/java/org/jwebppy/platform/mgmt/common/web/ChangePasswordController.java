package org.jwebppy.platform.mgmt.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyType;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyVo;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.FORM_AUTHENTICATION_PATH)
public class ChangePasswordController extends MgmtGeneralController
{
	@Autowired
	private CredentialsPolicyService credentialsPolicyService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

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

					//계정이 사용 기간이 유효한지 체크
					if (userAccount.isValidPeriod() && !userAccount.isAccountLocked())
					{
						//현재 비밀번호 체크
						if (passwordEncoder.matches(password, userAccount.getPassword()))
						{
							//현재 비밀번호와 신규 비밀번호가 동일한지 체크
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
								messages.add(i18nMessageSource.getMessage("PLTF_M_NOT_ALLOW_USE_CURRENT_PASSWORD"));
							}
						}
						else
						{
							messages.add(i18nMessageSource.getMessage("PLTF_M_NOT_CORRECT_PASSWORD"));
						}
					}
					else
					{
						messages.add(i18nMessageSource.getMessage("PLTF_M_INACTIVE_ACCOUNT"));
					}
				}
				else
				{
					messages.add(i18nMessageSource.getMessage("PLTF_M_NOT_EXIST_ACCOUNT"));
				}
			}
			else
			{
				messages.add(i18nMessageSource.getMessage("PLTF_M_NOT_PASSWORD_MATCH"));
			}
		}
		else
		{
			messages.add(i18nMessageSource.getMessage("PLTF_M_SESSION_EXPIRED"));
			resultMap.put("RESULT", -999);
		}

		resultMap.put("MESSAGE", messages);

		return resultMap;
	}
}
