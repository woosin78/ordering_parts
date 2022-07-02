package org.jwebppy.platform.mgmt.sso.web;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.AuthenticationHelper;
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.sso.uitils.CipherSecurity;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso/in/auth")
public class AuthenticationCheckController extends SsoController
{
	@Autowired
	private AuthenticationHelper authenticationHelper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	private static final Set<String> ALLOWABLE_SYSTEM = new HashSet<>();

	static {
		ALLOWABLE_SYSTEM.add("GPESWEB");
	}

	public static void main(String[] args)
	{
		try {
			System.err.println(URLEncoder.encode(new CipherSecurity().getEncryptDoobiz("P_IVDO01", "init99PW#", "GPESWEB"), "UTF-8"));
			System.err.println(URLEncoder.encode(new CipherSecurity().getEncryptDoobiz("P_IVEX01", "init99PW%", "GPESWEB"), "UTF-8"));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/check")
	@ResponseBody
	public Object check(@RequestParam(value = "key") String key)
	{
		try
		{
			String[] message = new CipherSecurity().getDecryptDoobiz(key);

			if (CmArrayUtils.isNotEmpty(message) && message.length == 3)
			{
				System.err.println("Username:" + message[0]);
				System.err.println("Password" + message[1]);
				System.err.println("System" + message[2]);

				if (ALLOWABLE_SYSTEM.contains(message[2]))
				{
					String username = CmStringUtils.trimToEmpty(message[0]).toUpperCase();
					String password = CmStringUtils.trimToEmpty(message[1]);

					UserDto user = userService.getUserByUsername(username);

					System.err.println("DoobizPlus User:" + user);

					if (ObjectUtils.isNotEmpty(user))
					{
						UserAccountDto userAccount = user.getUserAccount();

						System.err.println("Account ValidPeriod: " + userAccount.isValidPeriod());
						System.err.println("Account FgAccountLocked: " + userAccount.getFgAccountLocked());
						System.err.println("Account FgPasswordLocked: " + userAccount.getFgPasswordLocked());

						if (userAccount.isValidPeriod()
								&& CmStringUtils.equals(userAccount.getFgAccountLocked(), PlatformCommonVo.NO)
								&& CmStringUtils.equals(userAccount.getFgPasswordLocked(), PlatformCommonVo.NO))
						{
		            		//AD 인증에 성공했거나 비밀번호가 동일할 경우
		            		if (passwordEncoder.matches(password, userAccount.getPassword()))
		            		{
		            			System.err.println("Login Success on DoobizPlus");

		            			return PlatformCommonVo.SUCCESS;
		            		}
						}

						if (authenticationHelper.isDoobizUser(username, password))
						{
							System.err.println("Login Success on Doobiz");

							return PlatformCommonVo.SUCCESS;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.err.println("Login Fail");

		return PlatformCommonVo.FAIL;
	}
}
