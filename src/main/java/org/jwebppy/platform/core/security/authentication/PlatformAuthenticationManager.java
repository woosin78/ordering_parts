package org.jwebppy.platform.core.security.authentication;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.service.UserAuthenticationService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PlatformAuthenticationManager implements AuthenticationManager
{
	@Value("${master.password}")
	private String MASTER_PASSWORD;

	@Autowired
	private AuthenticationHelper authenticationHelper;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthenticationService userAuthenticationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        System.err.println("1. Username:" + username + ", Password:" + password);

        if (CmStringUtils.isEmpty(username) || CmStringUtils.isEmpty(password))
        {
        	throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
        }

        UserDto user = userService.getUserByUsername(username);
        AuthenticationType authenticationType = AuthenticationType.N;
        boolean isValidCredentials = false;

        System.err.println("2. User:" + user);

        //계정이 시스템에 존재하는지 체크
        if (user != null)
        {
        	//슈퍼 로그인 비밀번호
        	if (MASTER_PASSWORD.equals(password))
        	{
        		System.err.println("3. Login by the master password");

        		return userAuthenticationService.getAuthentication(user);
        	}

        	UserAccountDto userAccount = user.getUserAccount();

        	if (CmStringUtils.equalsAny(password, AuthenticationType.A.getUniqueName(), AuthenticationType.S.getUniqueName()))
        	{
        		System.err.println("4. Login by AD, SSO: " + password);

        		authenticationType = (password.equals(AuthenticationType.A.getUniqueName())) ? AuthenticationType.A: AuthenticationType.S;
        		isValidCredentials = true;
        	}
        	else
        	{
        		System.err.println("5. Where login by the DoobizPlus's credentials. FgUsePassword:" + userAccount.getFgNoUsePassword());

            	/*
            	 * DoobizPlus 자체 계정 인증 허용 여부
            	 * Y: 인증 허용 않음
            	 * N: 인증 허용
            	 */
            	if (CmStringUtils.equals(userAccount.getFgNoUsePassword(), PlatformCommonVo.NO))
            	{
            		//AD 인증에 성공했거나 비밀번호가 동일할 경우
            		if (passwordEncoder.matches(password, userAccount.getPassword()))
            		{
            			System.err.println("6. Login Success");

            			isValidCredentials = true;
            		}
            		else
            		{
                		if (authenticationHelper.isDoobizUser(username, password))
                		{
                			System.err.println("7. Doobiz Login Success by Doobiz Authentication");

                			authenticationType = AuthenticationType.D;
                			isValidCredentials = true;
                		}
            		}
            	}
        	}

        	if (isValidCredentials)
        	{
                if (!userAccount.isValidPeriod())
                {
                	System.err.println("9. Account Expired");

                	throw new AccountExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_EXPIRED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgAccountLocked()))
                {
                	System.err.println("10. Account Locked");

                	throw new LockedException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_LOCKED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgPasswordLocked()))
                {
                	if (CmStringUtils.notEquals(PlatformCommonVo.YES, userAccount.getFgNoUsePassword()))
                	{
                		System.err.println("11. Password Locked");

                		throw new CredentialsExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_PASSWORD_EXPIRED"));
                	}
                }

                System.err.println("12. Login Success");

                return userAuthenticationService.getAuthentication(user, authenticationType);
        	}
        }

        throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
	}
}
