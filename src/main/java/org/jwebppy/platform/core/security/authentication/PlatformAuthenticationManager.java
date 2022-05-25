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

        if (CmStringUtils.isEmpty(username) || CmStringUtils.isEmpty(password))
        {
        	throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
        }

        UserDto user = userService.getUserByUsername(username);
        boolean isValidCredentials = false;

        //계정이 시스템에 존재하는지 체크
        if (user != null)
        {
        	//슈퍼 로그인 비밀번호
        	if (MASTER_PASSWORD.equals(password))
        	{
        		return userAuthenticationService.getAuthentication(user);
        	}

        	UserAccountDto userAccount = user.getUserAccount();

        	if (CmStringUtils.equalsAny(password, "AD-USER", "SSO-USER"))
        	{
        		isValidCredentials = true;
        	}
        	else
        	{
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
            			isValidCredentials = true;
            		}
            	}
        	}

        	if (isValidCredentials)
        	{
                if (!userAccount.isValidPeriod())
                {
                	throw new AccountExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_EXPIRED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgAccountLocked()))
                {
                	throw new LockedException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_LOCKED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgPasswordLocked()))
                {
                	if (CmStringUtils.notEquals(PlatformCommonVo.YES, userAccount.getFgNoUsePassword()))
                	{
                		throw new CredentialsExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_PASSWORD_EXPIRED"));
                	}
                }

                return userAuthenticationService.getAuthentication(user);
        	}
        }

        throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
	}
}
